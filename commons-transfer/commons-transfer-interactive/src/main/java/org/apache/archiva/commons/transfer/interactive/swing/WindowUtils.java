package org.apache.archiva.commons.transfer.interactive.swing;

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

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * WindowUtils
 *
 * @author <a href="mailto:joakime@apache.org">Joakim Erdfelt</a>
 * @version $Id$
 */
public class WindowUtils
{
    public static void centerWindowOnParent( Window win )
    {
        Component comp = win.getParent();
        if ( comp == null )
        {
            centerWindowOnScreen( win );
            return;
        }

        win.setLocationRelativeTo( comp );
        Dimension compSize = comp.getSize();
        Point compCenter = new Point( compSize.width / 2, compSize.height / 2 );
        Point winCenterRelative = new Point( win.getSize().width / 2, win.getSize().height / 2 );
        win.setLocation( ( compCenter.x - winCenterRelative.x ), ( compCenter.y - winCenterRelative.y ) );
    }

    public static void centerWindowOnScreen( Window win )
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        Point screenCenter = new Point( screenSize.width / 2, screenSize.height / 2 );
        Point winCenterRelative = new Point( win.getSize().width / 2, win.getSize().height / 2 );
        win.setLocation( ( screenCenter.x - winCenterRelative.x ), ( screenCenter.y - winCenterRelative.y ) );
    }

    public static boolean isResizable( Window win )
    {
        boolean resizable = false;

        if ( win instanceof Frame )
        {
            resizable = ( (Frame) win ).isResizable();
        }
        else if ( win instanceof Dialog )
        {
            resizable = ( (Dialog) win ).isResizable();
        }

        return resizable;
    }
}
