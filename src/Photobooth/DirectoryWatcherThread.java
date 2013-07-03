package Photobooth;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.*;

public class DirectoryWatcherThread extends Thread
{
	private Path watchDirectory;
	private IDirectoryWatcherObserver observer;
	private WatchService watchService;
	private WatchKey watchKey;

	public DirectoryWatcherThread(String filepath, IDirectoryWatcherObserver observer) throws IOException
	{
		this.observer = observer;
		watchDirectory = FileSystems.getDefault().getPath(filepath);
		watchService = FileSystems.getDefault().newWatchService();
		watchKey = watchDirectory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
	}

	public void run()
	{
		while (!this.isInterrupted())
		{
			pollDirectory();
		}

		try
		{
			watchService.close();
		}
		catch (IOException e)
		{
			// We don't care if there's an exception, we're just cleaning up
		}
	}

	private void pollDirectory()
	{
		watchKey = watchService.poll();

		if (watchKey != null)
		{
			handleFileEvents();

			if (!watchKey.reset())
				return;
		}
		else
		{
			try
			{
				sleep(100);
			}
			catch (InterruptedException e)
			{
				return;
			}
		}
	}

	private void handleFileEvents()
	{
		for (WatchEvent<?> event: watchKey.pollEvents())
		{
			WatchEvent.Kind<?> kind = event.kind();

			if (kind == StandardWatchEventKinds.OVERFLOW)
				continue;

			WatchEvent<Path> watchEvent = (WatchEvent<Path>)event;
			Path filename = watchEvent.context();

			// TODO: Hopefully there's a platform-independant way of figuring this out...
			String fullpath = watchDirectory.toString() + "\\" + filename.toString();

			observer.onNewFile(fullpath);
		}
	}
}
