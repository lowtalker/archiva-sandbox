package org.apache.archiva.commons.transfer;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * MimeTypes
 *
 * @author <a href="mailto:joakime@apache.org">Joakim Erdfelt</a>
 * @version $Id$
 */
public class MimeTypes
{
    private static final Log LOG = LogFactory.getLog( MimeTypes.class );

    private Map<String, String> mimeMap = new HashMap<String, String>();

    public MimeTypes()
    {
        load( "commons-transfer/mime-types.txt" );
    }

    public MimeTypes( File mimeTypesFile )
    {
        load( mimeTypesFile );
    }

    public MimeTypes( InputStream stream )
    {
        load( stream );
    }

    public MimeTypes( String resourceName )
    {
        load( resourceName );
    }

    /**
     * Get the Mime Type for the provided filename.
     * 
     * @param filename the filename to obtain the mime type for.
     * @return a mime type String, or null if filename is null, has no extension, or no mime type is associated with it.
     */
    public String getMimeType( String filename )
    {
        if ( StringUtils.isEmpty( filename ) )
        {
            return null;
        }

        String ext = FilenameUtils.getExtension( filename );

        if ( StringUtils.isEmpty( ext ) )
        {
            return null;
        }

        return mimeMap.get( ext.toLowerCase() );
    }

    public void load( File file )
    {
        if ( !file.exists() || !file.isFile() || !file.canRead() )
        {
            LOG.error( "Unable to load mime types from file " + file.getAbsolutePath() + " : not a readable file." );
            return;
        }

        FileInputStream fis = null;

        try
        {
            fis = new FileInputStream( file );
        }
        catch ( FileNotFoundException e )
        {
            LOG.error( "Unable to load mime types from file " + file.getAbsolutePath() + " : " + e.getMessage(), e );
        }
        finally
        {
            IOUtils.closeQuietly( fis );
        }
    }

    public void load( InputStream mimeStream )
    {
        mimeMap.clear();

        InputStreamReader reader = null;
        BufferedReader buf = null;

        try
        {
            reader = new InputStreamReader( mimeStream );
            buf = new BufferedReader( reader );
            String line = null;

            while ( ( line = buf.readLine() ) != null )
            {
                line = line.trim();

                if ( line.length() == 0 )
                {
                    // empty line. skip it
                    continue;
                }

                if ( line.startsWith( "#" ) )
                {
                    // Comment. skip it
                    continue;
                }

                StringTokenizer tokenizer = new StringTokenizer( line );
                if ( tokenizer.countTokens() > 1 )
                {
                    String type = tokenizer.nextToken();
                    while ( tokenizer.hasMoreTokens() )
                    {
                        String extension = tokenizer.nextToken().toLowerCase();
                        mimeMap.put( extension, type );
                    }
                }
            }
        }
        catch ( IOException e )
        {
            LOG.error( "Unable to read mime types from input stream : " + e.getMessage(), e );
        }
        finally
        {
            IOUtils.closeQuietly( buf );
            IOUtils.closeQuietly( reader );
        }
    }

    public void load( String resourceName )
    {
        ClassLoader cloader = this.getClass().getClassLoader();

        /* Load up the mime types table */
        URL mimeURL = cloader.getResource( resourceName );

        if ( mimeURL == null )
        {
            throw new IllegalStateException( "Unable to find resource " + resourceName );
        }

        InputStream mimeStream = null;

        try
        {
            mimeStream = mimeURL.openStream();
            load( mimeStream );
        }
        catch ( IOException e )
        {
            LOG.error( "Unable to load mime map " + resourceName + " : " + e.getMessage(), e );
        }
        finally
        {
            IOUtils.closeQuietly( mimeStream );
        }
    }
}