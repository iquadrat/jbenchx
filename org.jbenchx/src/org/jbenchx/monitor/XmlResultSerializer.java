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
import org.w3c.dom.*;

public class XmlResultSerializer {

  public void serialize(IBenchmarkResult result, OutputStream out) {

    long startTime = result.getStartTime().getTime();
    long endTime = result.getEndTime().getTime();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    // factory.setNamespaceAware( true );
    // factory.setValidating( true );
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();

      Document document = builder.newDocument();
      Element benchmark = document.createElement("benchmark");
      benchmark.setAttribute("starttime", String.valueOf(startTime));
      benchmark.setAttribute("endtime", String.valueOf(endTime));
      document.appendChild(benchmark);

      for (IBenchmarkTask task: result.getTasks()) {
        ITaskResult taskResult = result.getResult(task);

        Element taskNode = document.createElement("task");
        benchmark.appendChild(taskNode);
        taskNode.setAttribute("name", task.getName());
        taskNode.setAttribute("iterations", String.valueOf(taskResult.getIterationCount()));
        taskNode.setAttribute("benchmark", String.valueOf(taskResult.getEstimatedBenchmark()));

        addParamNode(document, taskNode, taskResult.getTimings().getParams());
        addTimingsNode(document, taskNode, taskResult.getTimings());

        if (!taskResult.getFailures().isEmpty()) {
          addFailureNode(document, taskNode, taskResult.getFailures());
        }
        if (!taskResult.getWarnings().isEmpty()) {
          addWarningNode(document, taskNode, taskResult.getWarnings());
        }

      }

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      DOMSource source = new DOMSource(document);
      StreamResult streamResult = new StreamResult(out);
      transformer.transform(source, streamResult);

      out.close();

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
