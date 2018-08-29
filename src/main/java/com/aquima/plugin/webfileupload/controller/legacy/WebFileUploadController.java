package com.aquima.plugin.webfileupload.controller.legacy;

import com.aquima.interactions.foundation.logging.LogFactory;
import com.aquima.interactions.foundation.logging.Logger;
import com.aquima.interactions.portal.IPortalSession;
import com.aquima.plugin.webfileupload.FileUploadHandler;
import com.aquima.plugin.webfileupload.MultipartHttpRequest;
import com.aquima.plugin.webfileupload.controller.PortalSessionWrap;
import com.aquima.web.api.model.page.PageEvent;
import com.aquima.web.api.service.SessionService;
import com.aquima.web.api.service.events.EventResponse;
import com.aquima.web.api.service.subscription.SessionListener;

import com.blueriq.component.api.IAquimaSession;
import com.blueriq.component.api.IAquimaSessionsMap;
import com.blueriq.component.api.annotation.AquimaSessionId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller for uploading a file.
 * 
 * @author b.van.de.ven
 * @since 9.2
 */
@Controller("legacyWebFileUploadController")
@Deprecated
public class WebFileUploadController {

  private static final Logger LOG = LogFactory.getLogger(WebFileUploadController.class);

  private static final String SUBSCRIPTION_ID = "subscriptionId";
  private static final String SESSION_ID = "sessionId";

  private final FileUploadHandler fileUploadHandler;
  private final IAquimaSessionsMap sessionManager;
  private final SessionListener sessionListener;
  private final SessionService sessionService;

  /**
   * Constructs a new controller for uploading files.
   * 
   * @param sessionManager the {@link IAquimaSessionsMap} with the active sessions
   */
  @Autowired
  public WebFileUploadController(IAquimaSessionsMap sessionManager, SessionService sessionService,
      SessionListener sessionListener) {
    this.fileUploadHandler = new FileUploadHandler();
    this.sessionManager = sessionManager;
    this.sessionService = sessionService;
    this.sessionListener = sessionListener;
  }

  /**
   * Checks a regular {@link HttpServletRequest} for fileuploads.
   * 
   * @param request the {@link HttpServletRequest}
   * @param sessionId the sessionID of the session to upload files to
   * @return the {@link ModalAndView} to render
   */
  @RequestMapping(value = "/session/{sessionId}/api/subscription/{subscriptionId}/webfileupload/handle",
      method = { RequestMethod.POST })
  @ResponseBody
  public EventResponse handle(HttpServletRequest request, @PathVariable(SUBSCRIPTION_ID) IAquimaSession rootSession, //
      @PathVariable(SESSION_ID) @AquimaSessionId IAquimaSession currentSession) throws Exception {

    IPortalSession session = currentSession.getPortalSession();

    CommonsMultipartResolver resolver = new CommonsMultipartResolver();
    if (resolver.isMultipart(request)) {
      try {
        MultipartHttpRequest httpRequest;
        if (request instanceof MultipartHttpServletRequest) {
          httpRequest = new MultipartHttpRequest((MultipartHttpServletRequest) request);
        } else {
          httpRequest = new MultipartHttpRequest(resolver.resolveMultipart(request));
        }
        this.fileUploadHandler.handleUploads(new PortalSessionWrap(session), httpRequest);
      } catch (Exception e) {
        LOG.warning("Files could not be uploaded", e);
      }
    } else {
      throw new IllegalArgumentException("Request does not contains multipart content");
    }

    this.sessionService.handleEvent(session, new PageEvent());

    return new EventResponse(sessionListener.getEvents());
  }
}
