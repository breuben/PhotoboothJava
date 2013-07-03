package Photobooth;

import java.nio.file.Path;

public interface IDirectoryWatcherObserver
{
	public void onNewFile(String filepath);
}
