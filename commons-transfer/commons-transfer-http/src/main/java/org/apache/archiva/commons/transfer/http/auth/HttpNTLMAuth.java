package org.apache.archiva.commons.transfer.http.auth;

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

/**
 * HttpNTLMAuth
 *
 * @author <a href="mailto:joakime@apache.org">Joakim Erdfelt</a>
 * @version $Id$
 */
public class HttpNTLMAuth
{
    /** Required: key */
    private HttpAuthKey key = new HttpAuthKey();

    /** Optional: credentials */
    private NTCredentials credentials = new NTCredentials();

    /** Optional: persisted */
    private boolean persisted;

    public NTCredentials getCredentials()
    {
        return credentials;
    }

    public HttpAuthKey getKey()
    {
        return key;
    }

    public boolean isPersisted()
    {
        return persisted;
    }

    public void setCredentials( NTCredentials credentials )
    {
        this.credentials = credentials;
    }

    public void setKey( HttpAuthKey key )
    {
        this.key = key;
    }

    public void setPersisted( boolean persisted )
    {
        this.persisted = persisted;
    }
}
