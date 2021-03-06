package org.apache.archiva.commons.transfer.http.links;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

/**
 * LinkParserTest
 *
 * @author <a href="mailto:joakime@apache.org">Joakim Erdfelt</a>
 * @version $Id$
 */
public class LinkParserTest
    extends TestCase
{
    private void assertContainsExpected( Set<String> links, String[] expected )
    {
        for ( String expect : expected )
        {
            assertTrue( "Should find [" + expect + "] in link list", links.contains( expect ) );
        }
    }

    private void assertNotContainingAvoided( Set<String> links, String[] avoided )
    {
        for ( String avoid : avoided )
        {
            assertFalse( "Should not find [" + avoid + "] in link list", links.contains( avoid ) );
        }
    }

    private Set<String> parseLinks( String testfilename )
        throws IOException, SAXException, URISyntaxException
    {
        File testFile = new File( "src/test/resources/links", testfilename );
        Set<String> links = null;

        FileInputStream stream = null;
        try
        {
            stream = new FileInputStream( testFile );
            LinkParser linkParser = new LinkParser();
            links = linkParser.collectLinks( new URI( "http://localhost/" ), stream, null );
        }
        finally
        {
            IOUtils.closeQuietly( stream );
        }

        assertNotNull( "Set<String> links should not be null.", links );
        return links;
    }

    public void testCleanLink()
        throws URISyntaxException
    {
        LinkParser lp = new LinkParser();
        URI baseURI = new URI( "http://localhost/content/" );

        assertEquals( "", lp.cleanLink( baseURI, null ) );
        assertEquals( "", lp.cleanLink( baseURI, "" ) );
        assertEquals( "apache-archiva-1.0.1.jar", lp.cleanLink( baseURI, "./apache-archiva-1.0.1.jar" ) );
        assertEquals( "apache-archiva-1.0.1-sources.jar", lp.cleanLink( baseURI,
                                                                        "/content/apache-archiva-1.0.1-sources.jar" ) );
    }

    public void testLinkParseArchiva101()
        throws IOException, SAXException, URISyntaxException
    {
        Set<String> links = parseLinks( "archiva-1.0.1.html" );

        String[] expected = new String[] {
            "archiva-plexus-runtime-1.0.1-bin.zip",
            "archiva-plexus-runtime-1.0.1-bin.tar.gz",
            "archiva-plexus-runtime-1.0.1.jar",
            "archiva-plexus-runtime-1.0.1.pom",
            "archiva-plexus-runtime-1.0.1-sources.jar" };

        assertContainsExpected( links, expected );

        String[] avoided = new String[] {
            "/repo/m2-ibiblio-rsync-repository/org/apache/maven/archiva/archiva-plexus-runtime/",
            "?C=S;O=A",
            "?C=D;O=A",
            "?C=M;O=A" };

        assertNotContainingAvoided( links, avoided );
    }

    public void testLinkParseCommonsLang()
        throws IOException, SAXException, URISyntaxException
    {
        Set<String> links = parseLinks( "commons-lang.html" );

        String[] expected = new String[] {
            "maven-metadata.xml",
            "commons-lang-2.3-sources.jar",
            "commons-lang-2.3.jar.asc",
            "commons-lang-2.3.jar",
            "commons-lang-2.3.pom",
            "maven-metadata.xml.sha1",
            "commons-lang-2.3-javadoc.jar",
            "commons-lang-2.3.jar.sha1" };

        assertContainsExpected( links, expected );

        String[] avoided = new String[] { "../" };

        assertNotContainingAvoided( links, avoided );
    }

    public void testLinkParseMevenIde()
        throws IOException, SAXException, URISyntaxException
    {
        Set<String> links = parseLinks( "mevenide.html" );

        String[] expected = new String[] {
            "feature/",
            "maven-metadata.xml",
            "nb-project/",
            "deployment-bridge/",
            "indexer/",
            "autoupdate/" };

        assertContainsExpected( links, expected );

        String[] avoided = new String[] { "/org/codehaus/", "?C=S;O=A", "?C=D;O=A", "?C=M;O=A" };

        assertNotContainingAvoided( links, avoided );
    }

    public void testLinkParseNekoHtml()
        throws IOException, SAXException, URISyntaxException
    {
        Set<String> links = parseLinks( "nekohtml.html" );

        String[] expected = new String[] {
            "nekohtml-1.9.6.pom",
            "nekohtml-1.9.6.jar",
            "nekohtml-1.9.6-javadoc.jar",
            "nekohtml-1.9.6-sources.jar.sha1",
            "nekohtml-1.9.6-sources.jar",
            "nekohtml-1.9.6.pom.sha1" };

        assertContainsExpected( links, expected );

        String[] avoided = new String[] { "/maven2/nekohtml/nekohtml/", "?C=S;O=A", "?C=D;O=A", "?C=M;O=A" };

        assertNotContainingAvoided( links, avoided );
    }

    public void testLinkParseNetSourceforge()
        throws IOException, SAXException, URISyntaxException
    {
        Set<String> links = parseLinks( "net_sf.html" );

        String[] expected = new String[] {
            "speculoos/",
            "saxon/",
            "clirr/",
            "antenna/",
            "json-lib/",
            "jour/",
            "jsr107cache/",
            "kxml/",
            "aislib/",
            "opencsv/",
            "maven-sar/",
            "gwt-widget/",
            "ldaptemplate/",
            "mapasuta/",
            "click/",
            "proguard/",
            "locale4j/",
            "jlynx/",
            "alchim/",
            "jcharts/",
            "ezmorph/",
            "dozer/",
            "jxls/",
            "smc/",
            "hibernate/",
            "tacos/",
            "resultsetmapper/",
            "stat-scm/",
            "beanlib/" };

        assertContainsExpected( links, expected );

        String[] avoided = new String[] { "/maven2/net/", "?C=S;O=A", "?C=D;O=A", "?C=M;O=A" };

        assertNotContainingAvoided( links, avoided );
    }

    public void testLinkParseOrgCodehaus()
        throws IOException, SAXException, URISyntaxException
    {
        Set<String> links = parseLinks( "org.codehaus.html" );

        String[] expected = new String[] {
            "xdoclet/",
            "labs-ng/",
            "droolsdotnet/",
            "testdox/",
            "prometheus/",
            "cargo/",
            "staxmate/",
            "groovy/",
            "bruce/",
            "redback/",
            "mvel/",
            "waffle/",
            "fabric3/",
            "rvm/",
            "quaere/",
            "enunciate/",
            "prophit/",
            "gant/",
            "benji/",
            "jremoting/",
            "xsite/",
            "savana/",
            "dataforge/",
            "rulessandpit/",
            "castor-spring/",
            "jet/",
            "openim/",
            "senro/",
            "sonar/",
            "gwt-openlayers/",
            "wadi/",
            "swiby/",
            "izpack/",
            "mojo/",
            "plexus/",
            "stomp/",
            "gumtree/",
            "mvflex/",
            "grails-plugins/",
            "svn4j/",
            "scala-ide/",
            "sxc/",
            "xstream/" };

        assertContainsExpected( links, expected );

        String[] avoided = new String[] { "/org/", "?C=S;O=A", "?C=D;O=A", "?C=M;O=A" };

        assertNotContainingAvoided( links, avoided );
    }
}
