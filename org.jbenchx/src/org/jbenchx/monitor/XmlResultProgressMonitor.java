/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.monitor;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.jbenchx.*;
import org.jbenchx.result.*;
import org.jbenchx.util.*;
import org.w3c.dom.*;

import edu.umd.cs.findbugs.annotations.*;

public class XmlResultProgressMonitor extends IProgressMonitor.Stub implements IProgressMonitor {

  private final OutputStream  fOutputStream;

  private final ITimeProvider fTimeProvider;

  @CheckForNull
  private IBenchmarkResult    fResult    = null;

  private long                fStartTime = Long.MIN_VALUE;

  private long                fEndTime   = Long.MIN_VALUE;

  public XmlResultProgressMonitor(OutputStream out) {
    this(out, TimeUtil.getDefaultTimeProvider());
  }

  public XmlResultProgressMonitor(OutputStream out, ITimeProvider timeProvider) {
    fOutputStream = out;
    fTimeProvider = timeProvider;
  }

  @Override
  public void init(int count, IBenchmarkResult result) {
    fResult = result;
    fStartTime = fTimeProvider.getCurrentTimeMs();
  }

  @Override
  public void finished() {
    Assert.isNotNull(fResult);

    fEndTime = fTimeProvider.getCurrentTimeMs();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    // factory.setNamespaceAware( true );
    // factory.setValidating( true );
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();

      Document document = builder.newDocument();
      Element benchmark = document.createElement("benchmark");
      benchmark.setAttribute("starttime", String.valueOf(fStartTime));
      benchmark.setAttribute("endtime", String.valueOf(fEndTime));
      document.appendChild(benchmark);

      for (IBenchmarkTask task: fResult.getTasks()) {
        ITaskResult result = fResult.getResult(task);

        Element taskNode = document.createElement("task");
        benchmark.appendChild(taskNode);
        taskNode.setAttribute("name", task.getName());
        taskNode.setAttribute("iterations", String.valueOf(result.getIterationCount()));
        taskNode.setAttribute("benchmark", String.valueOf(result.getEstimatedBenchmark()));

        addParamNode(document, taskNode, result.getTimings().getParams());
        addTimingsNode(document, taskNode, result.getTimings());

        if (!result.getFailures().isEmpty()) {
          addFailureNode(document, taskNode, result.getFailures());
        }
        if (!result.getWarnings().isEmpty()) {
          addWarningNode(document, taskNode, result.getWarnings());
        }

      }

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      DOMSource source = new DOMSource(document);
      StreamResult result = new StreamResult(fOutputStream);
      transformer.transform(source, result);

      fOutputStream.flush();

    } catch (ParserConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (TransformerConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (TransformerFactoryConfigurationError e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (TransformerException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  private void addParamNode(Document document, Element taskNode, BenchmarkParameters params) {
    Element paramsNode = document.createElement("params");
    paramsNode.setAttribute("divisor", String.valueOf(params.getDivisor()));
    paramsNode.setAttribute("maxDeviation", String.valueOf(params.getMaxDeviation()));
    paramsNode.setAttribute("maxRunCount", String.valueOf(params.getMaxRunCount()));
    paramsNode.setAttribute("minRunCount", String.valueOf(params.getMinRunCount()));
    paramsNode.setAttribute("minSampleCount", String.valueOf(params.getMinSampleCount()));
    paramsNode.setAttribute("targetTime", String.valueOf(params.getTargetTimeNs()));
    taskNode.appendChild(paramsNode);
  }

  private void addTimingsNode(Document document, Element taskNode, BenchmarkTimings timings) {
    for (Timing timing: timings.getTimings()) {
      Element timingNode = document.createElement("run");
      timingNode.setAttribute("runtime", String.valueOf(timing.getRunTime()));
      taskNode.appendChild(timingNode);
      for (String gcName: timing.getGcNames()) {
        Element gcNode = document.createElement("gc");
        gcNode.setAttribute("name", gcName);
        gcNode.setAttribute("count", String.valueOf(timing.getGcCount(gcName)));
        gcNode.setAttribute("time", String.valueOf(timing.getGcTime(gcName)));
        timingNode.appendChild(gcNode);
      }
    }
  }

  private void addWarningNode(Document document, Element taskNode, List<BenchmarkWarning> warnings) {
    for (BenchmarkWarning warning: warnings) {
      Element warningNode = document.createElement("warning");
      warningNode.setTextContent(warning.getReason());
      taskNode.appendChild(warningNode);
    }
  }

  private void addFailureNode(Document document, Element taskNode, List<BenchmarkFailure> failures) {
    for (BenchmarkFailure failure: failures) {
      Element failureNode = document.createElement("failure");
      StringWriter writer = new StringWriter();
      failure.print(new PrintWriter(writer));
      failureNode.setTextContent(writer.getBuffer().toString());
      taskNode.appendChild(failureNode);
    }
  }

}
