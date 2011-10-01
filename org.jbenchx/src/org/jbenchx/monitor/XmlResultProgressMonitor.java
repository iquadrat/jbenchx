/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.monitor;

import javax.xml.parsers.*;

import org.jbenchx.*;
import org.jbenchx.result.*;
import org.jbenchx.util.*;
import org.w3c.dom.*;

import edu.umd.cs.findbugs.annotations.*;



public class XmlResultProgressMonitor extends IProgressMonitor.Stub implements IProgressMonitor {

  @CheckForNull
  private IBenchmarkResult fResult = null;

  @Override
  public void init(int count, IBenchmarkResult result) {
    fResult = result;
  }

  @Override
  public void finished()  {
    Assert.isNotNull(fResult);

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    // factory.setNamespaceAware( true );
    // factory.setValidating( true );
    try {
      DocumentBuilder builder  = factory.newDocumentBuilder();

      Document document = builder.newDocument();
      System.out.println(document.getDocumentElement());

    } catch (ParserConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    for(IBenchmarkTask task: fResult.getTasks()) {
      ITaskResult result = fResult.getResult(task);
    }

  }

}
