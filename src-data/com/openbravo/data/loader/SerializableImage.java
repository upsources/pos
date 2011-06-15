/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.data.loader;

/**
 *
 * @author Mohamed Said Lokhat
 */
import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.io.Serializable;
//import com.openbravo.data.loader.SerializableWrite;
//import com.openbravo.data.loader.SerializableRead;
//import com.openbravo.basic.BasicException;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import java.io.*;
//import com.openbravo.pos.util.StringUtils;
//import com.openbravo.data.loader.DataRead;

//import com.openbravo.data.loader.DataWrite;
//import com.openbravo.format.Formats;

import com.openbravo.basic.BasicException;
//import com.openbravo.pos.forms.AppLocal;
//import java.util.Properties;

public class SerializableImage implements SerializableWrite, SerializableRead,Serializable
{

	private BufferedImage image = null;

	public SerializableImage()
	{
		super();
	}

	public SerializableImage(BufferedImage im)
	{
		this();
		setImage(im);
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public void setImage(BufferedImage img)
	{
		this.image = img;
	}

        @Override
        public void writeValues(DataWrite dp) throws BasicException {
            dp.setBytes(1,  ImageUtils.writeImage(image));
            
        }

        @Override
        public void readValues(DataRead dr) throws BasicException {

            image = ImageUtils.readImage(dr.getBytes(1));


        }

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
                if (image != null ) {
                    ImageIO.write(getImage(), "jpg", new MemoryCacheImageOutputStream(out));
                }
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		setImage(ImageIO.read(new MemoryCacheImageInputStream(in)));
	}
}
