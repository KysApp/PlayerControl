/*
 * Copyright (C) 2013 4th Line GmbH, Switzerland
 *
 * The contents of this file are subject to the terms of either the GNU
 * Lesser General Public License Version 2 or later ("LGPL") or the
 * Common Development and Distribution License Version 1 or later
 * ("CDDL") (collectively, the "License"). You may not use this file
 * except in compliance with the License. See LICENSE.txt for more
 * information.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.fourthline.cling.support.avtransport.callback;

import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;

import java.util.logging.Logger;

/**
 * @author Christian Bauer
 */
public abstract class SetAVTransportURI extends ActionCallback {

    private static Logger log = Logger.getLogger(SetAVTransportURI.class.getName());

    public SetAVTransportURI(Service service, String uri) {
        this(new UnsignedIntegerFourBytes(0), service, uri, null);
    }

    public SetAVTransportURI(Service service, String uri, String metadata) {
        this(new UnsignedIntegerFourBytes(0), service, uri, metadata);
    }

    public SetAVTransportURI(UnsignedIntegerFourBytes instanceId, Service service, String uri) {
        this(instanceId, service, uri, null);
    }

    public SetAVTransportURI(UnsignedIntegerFourBytes instanceId, Service service, String uri, String metadata) {
        super(new ActionInvocation(service.getAction("SetAVTransportURI")));
        log.fine("Creating SetAVTransportURI action for URI: " + uri);
        getActionInvocation().setInput("InstanceID", instanceId);
        getActionInvocation().setInput("CurrentURI", uri);
        getActionInvocation().setInput("CurrentURIMetaData", "<DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\">   <item id=\"123\" parentID=\"-1\" restricted=\"1\">      <upnp:storageMedium>UNKNOWN</upnp:storageMedium>      <upnp:writeStatus>UNKNOWN</upnp:writeStatus>      <upnp:class>object.item.videoItem.movie</upnp:class>      <dc:creator>TencentVideo</dc:creator>      <TencentVideo-Params ver=\"1\" cid=\"lrwweimk8hanlk8\" vid=\"t00205zggor\" defn=\"hd\">txvideo://v.qq.com/VideoDetailActivity?expansion=jump_from%3djimu&amp;cid=lrwweimk8hanlk8&amp;isAutoPlay=0</TencentVideo-Params>      <dc:title>t00205zggor</dc:title>      <res protocolInfo=\"http-get:*:video/mp4:*\">http://117.145.182.13/vlivehls.tc.qq.com/mp4/8/3opAGAaLFBUgyLo3y1F_I3m66qhorS7-SQ-Y_KSEFFU9d42ld41ixQ/OGz-CAwVheP0zHlYBBHAUgOkfdWEy334m1YXSaKltKDwTA1-FrCCdmoWWOAl8Y8vpQFBSIapOYcQE9U0BUDhP3Wmoi1CFsUR_xqRkRcsOl2GTx7ENIyoDf1_6-NzNpC6bylDU98ivtSOXegldH024w/t00205zggor.p412.mp4/playlist.av.m3u8?fn=p412&amp;bw=500000&amp;st=0&amp;et=0&amp;iv=&amp;ivfn=&amp;ivfc=&amp;ivt=&amp;ivs=&amp;ivd=&amp;ivl=&amp;ftype=mp4&amp;fbw=46&amp;type=m3u8&amp;drm=0&amp;hlskey=&amp;sdtfrom=v5000&amp;guid=fd0dfbe74eae1033bbcf80fb0722850a</res>   </item></DIDL-Lite>");
    }

    @Override
    public void success(ActionInvocation invocation) {
        log.fine("Execution successful");
    }
}