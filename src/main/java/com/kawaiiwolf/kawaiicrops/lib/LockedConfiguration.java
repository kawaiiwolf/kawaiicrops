package com.kawaiiwolf.kawaiicrops.lib;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.google.common.primitives.Bytes;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraftforge.common.config.Configuration;

public class LockedConfiguration extends Configuration 
{
    public LockedConfiguration() { }

    public LockedConfiguration(File file)
    {
    	super(file);
    }
    
    public LockedConfiguration(File file, String configVersion)
    {
    	super(file, configVersion);
    }

    public LockedConfiguration(File file, String configVersion, boolean caseSensitiveCustomCategories)
    {
    	super(file, configVersion, caseSensitiveCustomCategories);
    }

    public LockedConfiguration(File file, boolean caseSensitiveCustomCategories)
    {
    	super(file, caseSensitiveCustomCategories);
    }
    
    //---------------------------------------------------------------------------------------------
    
    public static final String HEADER_COMMENT = ""
    		+ "This configuration file will determine if the other configuration files should be compressed.\n"
    		+ "Compressing a file is a good way to lock down your configurations so people don't alter them\n"
    		+ "or clutter up the configuration directory and help save space when distributing file packs.\n"
    		+ "configurations.\n"
    		+ "\n"
    		+ "State Options:\n"
    		+ "\n"
    		+ "- LOCKED:   Configuration files are zipped. A server won't let you on unless you're using the same\n"
    		+ "            configuration files. This is the best option for official distributions & servers.\n"
    		+ "\n"
    		+ "- UNLOCKED: No configuration files are zipped, server won't validate same configurations. This\n"
    		+ "            is the best option for development.\n"
    		+ "\n"
    		+ "- LOCK:     Convert from UNLOCK to LOCKED state. Config files will be zipped up and this config\n"
    		+ "            will be updated to LOCKED.\n"
    		+ "\n"
    		+ "- UNLOCK:   Convert from LOCKED to UNLOCKED state. Config files will be unzipped and this config\n"
    		+ "            will be updated to UNLOCKED.\n"
    		+ "";
    
    public enum LockedState { LOCKED, UNLOCKED, LOCK, UNLOCK };

    private static HashSet<String> configsLoaded = new HashSet<String>();
    private static HashSet<File> configsIncluded = new HashSet<File>();
    private static LockedState state = null;
	private static final int bufferSize = 1024;
    
	private static String digest = "";
	public static String digest() { return digest; }
	
    @Override
    public void load()
    {
    	if(configsLoaded.size() == 0)
    	{
    		String path = getConfigFile().getPath().substring(0, getConfigFile().getPath().length() - getConfigFile().getName().length());
    		File file = new File(path + Constants.CONFIG_ZIP_NAME);
    		
    		Configuration lock_cfg = new Configuration(new File(path + Constants.CONFIG_LOCK));
    		lock_cfg.load();
    		lock_cfg.setCategoryComment("state", HEADER_COMMENT);
    		
    		String stateString = lock_cfg.getString("state", "state", file.exists() ? "LOCKED" : "UNLOCKED", "Options: [LOCKED, UNLOCKED, LOCK or UNLOCK]");
    		
    		if (stateString.toLowerCase().equals("locked"))		 state = LockedState.LOCKED;
    		else if (stateString.toLowerCase().equals("lock"))	 state = LockedState.LOCK;
    		else if (stateString.toLowerCase().equals("unlock")) state = LockedState.UNLOCK;
    		else 												 state = LockedState.UNLOCKED;
    		
    		if (((state == LockedState.LOCKED || state == LockedState.UNLOCK) && !file.exists()) || (state == LockedState.UNLOCKED && !stateString.toLowerCase().equals("unlocked")))
    		{
    			state = LockedState.UNLOCKED;
    			lock_cfg.get("state", "state", "").set("UNLOCKED");
    		}
    		
    		if (state == LockedState.LOCK)
    			lock_cfg.get("state", "state", "").set("LOCKED");
    		if (state == LockedState.UNLOCK)
    			lock_cfg.get("state", "state", "").set("UNLOCKED");
    		
    		lock_cfg.save();
    		
    		if (state == LockedState.LOCKED || state == LockedState.UNLOCK)
    		{
    			// Unzip Files
    			try
    			{
    				ZipFile zip = new ZipFile(file);
    				Enumeration<? extends ZipEntry> entries = zip.entries();
    				ZipEntry entry;
            		byte buffer[] = new byte[bufferSize];
            		int count;
            		
    				while (entries.hasMoreElements())
    				{
    					entry = entries.nextElement();
    					File f = new File(path + entry.getName());
    					InputStream in = zip.getInputStream(entry);
    					OutputStream out = new BufferedOutputStream(new FileOutputStream(f), bufferSize);
    					
    					while ((count = in.read(buffer, 0, bufferSize)) != -1)
    						out.write(buffer, 0, count);
    					
    					in.close();
    					out.close();
    				}
    				zip.close();
    				
    			} catch (Exception e) { throw new RuntimeException(e); }
    		}
    		
    		if (state == LockedState.LOCKED)
    		{
    			try
    			{
    				// Calculate zip digest
    				
    				MessageDigest md = MessageDigest.getInstance("MD5");
    				byte buffer[] = new byte[bufferSize];
            		int count;
            		
            		InputStream in = new DigestInputStream(new BufferedInputStream(new FileInputStream(file),bufferSize), md);
            		while ((count = in.read(buffer, 0, bufferSize)) != -1) ;
            		in.close();
            		
            		digest = new String(md.digest());
    				
    			} catch (Exception e) { throw new RuntimeException(e); }
    		}
    	}
    	configsLoaded.add(getConfigFile().getName());
    	configsIncluded.add(getConfigFile());
    	
    	super.load();
    }
    
    @Override
    public void save()
    {
    	super.save();
    	
    	configsLoaded.remove(getConfigFile().getName());
    	if (configsLoaded.size() == 0)
    	{
    		String path = getConfigFile().getPath().substring(0, getConfigFile().getPath().length() - getConfigFile().getName().length());
    		File file = new File(path + Constants.CONFIG_ZIP_NAME);

    		if (state == LockedState.UNLOCK || state == LockedState.LOCK)
    		{
    			// Delete zip file
				if (file.exists()) 
					file.delete();
    		}
    		
    		if (state == LockedState.LOCK)
    		{
    			// Zip config and delete files
    			
        		byte buffer[] = new byte[bufferSize];
        		int count;
    			
    			try
    			{
    				// Write config files to zip
    				
    				if (file.exists()) file.delete();
    				ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
    				
    				for (File f : configsIncluded)
    				{
    					BufferedInputStream in = new BufferedInputStream(new FileInputStream(f), bufferSize);
    					ZipEntry entry = new ZipEntry(f.getName());
    					out.putNextEntry(entry);
    					
    					while ((count = in.read(buffer, 0, bufferSize)) != -1)
    						out.write(buffer, 0, count);
    					in.close();
    				}
    				out.close();
    				
    			} catch (Exception e) { throw new RuntimeException(e); }
    		}
    		
    		if (state == LockedState.LOCKED || state == LockedState.LOCK)
    		{
    			// Delete cfg files
  				for (File f : configsIncluded)
					f.delete();
    		}
    	}
    }
}
