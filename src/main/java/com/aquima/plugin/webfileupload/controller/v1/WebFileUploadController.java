package com.aquima.plugin.webfileupload.controller.v1;

import com.aquima.interactions.foundation.logging.LogFactory;
import com.aquima.interactions.foundation.logging.Logger;
import com.aquima.interactions.portal.IPortalSession;
import com.aquima.plugin.webfileupload.FileUploadHandler;
import com.aquima.plugin.webfileupload.MultipartHttpRequest;
import com.aquima.plugin.webfileupload.controller.PortalSessionWrap;
import com.aquima.web.api.controller.v1.AbstractBaseController;
import com.aquima.web.api.model.page.PageEvent;
import com.aquima.web.api.service.SessionService;
import com.aquima.web.api.service.events.EventResponse;
import com.aquima.web.api.service.subscription.SessionListener;

import com.blueriq.component.api.IAquimaSession;
import com.blueriq.component.api.annotation.AquimaSessionId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller for uploading a file.
 *
 * @author b.van.de.ven
 * @since 9.2
 */
@RestController("apiWebFileUploadController")
@RequestMapping(path = "/api/v1")
public class WebFileUploadController extends AbstractBaseController {

  private static final Logger LOG = LogFactory.getLogger(WebFileUploadController.class);

  private static final String SUBSCRIPTION_ID = "subscriptionId";
  private static final String SESSION_ID = "sessionId";

  private final FileUploadHandler fileUploadHandler;
  private final SessionListener sessionListener;
  private final SessionService sessionService;

  @Autowired
  public WebFileUploadController(SessionService sessionService, SessionListener sessionListener) {
    this.fileUploadHandler = new FileUploadHandler();
    this.sessionService = sessionService;
    this.sessionListener = sessionListener;
  }

  /**
   * Checks a regular {@link HttpServletRequest} for fileuploads.
   *
   * @param request the {@link HttpServletRequest}
   * @param session the session to upload files to
   * @return the {@link EventResponse} to render
   */
  @PostMapping("/subscription/{subscriptionId}/session/{sessionId}/webfileupload/handle")
  public EventResponse handle( //
      @PathVariable(SUBSCRIPTION_ID) IAquimaSession rootSession,
      @PathVariable(SESSION_ID) @AquimaSessionId IAquimaSession currentSession, HttpServletRequest request)
      throws Exception {
    IPortalSession portalSession = currentSession.getPortalSession();

    CommonsMultipartResolver resolver = new CommonsMultipartResolver();
    if (resolver.isMultipart(request)) {
      try {
        MultipartHttpRequest httpRequest;
        if (request instanceof MultipartHttpServletRequest) {
          httpRequest = new MultipartHttpRequest((MultipartHttpServletRequest) request);
        } else {
          httpRequest = new MultipartHttpRequest(resolver.resolveMultipart(request));
        }
        this.fileUploadHandler.handleUploads(new PortalSessionWrap(portalSession), httpRequest);
      } catch (Exception e) {
        LOG.warning("Files could not be uploaded", e);
      }
    } else {
      throw new IllegalArgumentException("Request does not contains multipart content");
    }

    this.sessionService.handleEvent(portalSession, new PageEvent());

    return new EventResponse(sessionListener.getEvents());
  }
}
