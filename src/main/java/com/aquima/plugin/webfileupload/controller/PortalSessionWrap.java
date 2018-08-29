package com.aquima.plugin.webfileupload.controller;

import com.aquima.interactions.portal.IActionHandler;
import com.aquima.interactions.portal.IPortalSession;
import com.aquima.interactions.portal.PortalEvent;

import java.util.Map;

public class PortalSessionWrap {

  private final IPortalSession session;

  public PortalSessionWrap(IPortalSession session) {
    this.session = session;
  }

  public void handleEvent(PortalEvent portalEvent) {
    this.session.handleEvent(portalEvent);
  }

  public void executeInlineAction(IActionHandler saveFilesHandler, @SuppressWarnings("rawtypes") Map object) {
    this.session.executeInlineAction(saveFilesHandler, object);
  }
}
