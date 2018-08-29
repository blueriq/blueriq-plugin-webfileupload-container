package com.aquima.plugin.webfileupload;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.aquima.interactions.composer.model.Container;
import com.aquima.interactions.composer.model.definition.ContainerDefinition;
import com.aquima.interactions.foundation.DataType;
import com.aquima.interactions.framework.ScopeKeys;
import com.aquima.interactions.portal.IContainerContext;
import com.aquima.interactions.portal.IContainerExpander;
import com.aquima.interactions.test.templates.ApplicationTemplate;
import com.aquima.interactions.test.templates.composer.ContainerTemplate;
import com.aquima.interactions.test.templates.composer.PageTemplate;
import com.aquima.interactions.test.templates.model.EntityTemplate;
import com.aquima.interactions.test.templates.session.PortalSessionTestFacade;
import com.aquima.plugin.webfileupload.controller.PortalSessionWrap;
import com.aquima.plugin.webfileupload.expander.SingleFileUploadContainer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WebFileUploadTest {

  private PortalSessionTestFacade session;
  private boolean hasFile;
  private final String entityAttribute = "Entity.Attribute";

  @Test
  public void testDefault() throws Exception {
    ApplicationTemplate app = this.createApplicationTemplate();
    this.session = new PortalSessionTestFacade(app);

    this.session.startFlow("start");

    assertNotNull(this.session.getCurrentPage());
    assertEquals("page", this.session.getCurrentPage().getName());

    // upload file
    List<FileAttachment> attachments = new ArrayList<>();

    attachments.add(new FileAttachment(this.entityAttribute, new RequestFileTest("testFileName", new byte[] { 1 })));
    FileUploadHandler.saveFiles(new PortalSessionWrap(this.session.getPortalSession()), attachments);

    this.session.recomposePage();

    assertEquals(((Container) this.session.getCurrentPage(true).getElement(0)).getElement(1).getName(), "successItem");
    assertTrue(this.hasFile);
  }

  private ApplicationTemplate createApplicationTemplate() {
    ApplicationTemplate applicationTemplate = new ApplicationTemplate("webfileupload");

    applicationTemplate.getProject().addExpander("AQ_Web_FileUpload");
    applicationTemplate.getProject().addExpander("TestContainer");
    applicationTemplate.getProject().addTheme("default");
    applicationTemplate.getProject().addTheme("default-json");
    applicationTemplate.getComposer().addText("successItem");
    applicationTemplate.getFlowEngine().addFlow("start").addPage("page");

    EntityTemplate entity = applicationTemplate.getMetaModel().addEntity("Entity", null, true);
    entity.addAttribute("Attribute", DataType.STRING, false);

    ContainerTemplate container = applicationTemplate.getComposer().addContainer("container");
    container.setTypeName("AQ_Web_FileUpload");
    container.addParameter("FileName", this.entityAttribute);
    container.addParameter("SuccessText", "successItem");

    ContainerTemplate container2 = applicationTemplate.getComposer().addContainer("container2");
    container2.setTypeName("TestContainer");

    PageTemplate page = applicationTemplate.getComposer().addPage("page");
    page.addContainer("container");
    page.addContainer("container2");

    applicationTemplate.getFactoryManager().getContainerFactory().addExpander("AQ_Web_FileUpload",
        new SingleFileUploadContainer());
    applicationTemplate.getFactoryManager().getContainerFactory().addExpander("TestContainer", new TestContainer());

    return applicationTemplate;
  }

  private class TestContainer implements IContainerExpander {

    public TestContainer() {}

    @Override
    public Container expand(Container container, ContainerDefinition definition, IContainerContext context)
        throws Exception {
      WebFileUploadTest.this.hasFile = context.getFlowScope().hasAttribute(ScopeKeys.FILE_KEY);
      return container;
    }
  }

}
