package Photobooth;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PhotoboothConfigModel
{
	public String CaptureDirectory;
	public String CaptureExtension;
	public int PreviewSeconds;
	public String FontName;
	public double FontSize;
	public String PromptText;

	private static String getCurrentDirectory()
	{
		return Paths.get("").toAbsolutePath().toString();
	}

	private static String getConfigFilename()
	{
		return getCurrentDirectory() + File.separator + "photobooth.config";
	}

	public static PhotoboothConfigModel GetDefaultConfiguration()
	{
		PhotoboothConfigModel config = new PhotoboothConfigModel();
		config.CaptureDirectory = Paths.get("").toAbsolutePath().toString();
		config.CaptureExtension = "*.jpg";
		config.PreviewSeconds = 5;
		config.FontName = "Gabriola";
		config.FontSize = 90;
		config.PromptText = "Press the button on the remote to take a picture.\nDon't forget to smile!";

		return config;
	}

	public static PhotoboothConfigModel ReadSavedConfiguration()
	{
		List<String> lines = readAllConfigLines();
		if (lines.size() == 0)
			return GetDefaultConfiguration();

		PhotoboothConfigModel config = new PhotoboothConfigModel();

		int i = 0;

		for (String line : lines)
		{
			switch (i)
			{
				case 0:
					config.CaptureDirectory = line;
					break;
				case 1:
					config.CaptureExtension = line;
					break;
				case 2:
					config.PreviewSeconds = Integer.parseInt(line);
					break;
				case 3:
					config.FontName = line;
					break;
				case 4:
					config.FontSize = Double.parseDouble(line);
					break;
				case 5:
					config.PromptText = line;
					break;
				default:
					config.PromptText += "\n" + line;
					break;
			}
			i++;
		}

		return config;
	}

	private static List<String> readAllConfigLines()
	{
		List<String> lines = new ArrayList<String>();
		try
		{
			String configFilename = getConfigFilename();
			BufferedReader br = new BufferedReader(new FileReader(configFilename));
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			br.close();
		}
		catch (Exception e)
		{ }

		return lines;
	}

	public static void PersistConfiguration(PhotoboothConfigModel config)
	{
		try
		{
			String configFilename = getConfigFilename();
			BufferedWriter w = new BufferedWriter(new FileWriter(configFilename));

			w.write(config.CaptureDirectory);
			w.newLine();
			w.write(config.CaptureExtension);
			w.newLine();
			w.write(Integer.toString(config.PreviewSeconds));
			w.newLine();
			w.write(config.FontName);
			w.newLine();
			w.write(Double.toString(config.FontSize));
			w.newLine();
			w.write(config.PromptText);

			w.close();

		}
		catch (Exception e)
		{ }
	}
}
