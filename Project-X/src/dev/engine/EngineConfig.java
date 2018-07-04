package dev.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

public class EngineConfig {

	private static EngineConfig instance;

	private HashMap<String, String> configuration;

	private HashMap<String, Integer> cacheInt;
	private HashMap<String, Float> cacheFloat;
	private HashMap<String, String> cacheString;
	private HashMap<String, Vector3f> cacheVector3f;

	public static final float ANISOTROPIC_FILTERING_LEVEL = 4f;

	private EngineConfig() {
		configuration = new HashMap<>();

		try {
			loadConfig("res/config.ini");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		// initialize the caches
		cacheInt = new HashMap<>();
		cacheFloat = new HashMap<>();
		cacheString = new HashMap<>();
		cacheVector3f = new HashMap<>();
	}

	public static EngineConfig getInstance() {
		if (instance == null)
			instance = new EngineConfig();

		return instance;
	}

	public int getInt(String key) {
		if (!isCached(key)) {
			int value = Integer.parseInt(configuration.get(key));

			cacheInt.put(key, value);
		}

		return cacheInt.get(key);
	}

	public float getFloat(String key) {
		if (!isCached(key)) {
			float value = Float.parseFloat(configuration.get(key));

			cacheFloat.put(key, value);
		}

		return cacheFloat.get(key);
	}

	public String getString(String key) {
		// we don't need for cache for strings
		return configuration.get(key);
	}

	public Vector3f getVector3f(String key) {
		if (!isCached(key)) {
			Vector3f value = new Vector3f();
			String[] tokens = configuration.get(key).trim().replaceAll("\\s\\s+", "").split(",");

			value.x = Float.parseFloat(tokens[0]);
			value.y = Float.parseFloat(tokens[1]);
			value.z = Float.parseFloat(tokens[2]);

			cacheVector3f.put(key, value);
		}

		return cacheVector3f.get(key);
	}

	private boolean isCached(String key) {
		boolean flag = true;

		flag = flag && cacheInt.containsKey(key);
		flag = flag && cacheFloat.containsKey(key);
		flag = flag && cacheString.containsKey(key);
		flag = flag && cacheVector3f.containsKey(key);

		return flag;
	}

	private void loadConfig(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));

		String line;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.length() == 0 || line.charAt(0) == '[' || !line.contains("="))
				continue;

			String[] tokens = line.trim().replaceAll("\\s\\s+", "").split("=");
			configuration.put(tokens[0].trim(), tokens[1].trim());
		}

		br.close();
	}

}
