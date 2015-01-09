/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.result;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

import org.jbenchx.BenchmarkParameters;
import org.jbenchx.run.ParameterizationValues;
import org.jbenchx.util.ObjectUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public class XmlResultSerializer {
  
  private static final Charset UTF8 = Charset.forName("UTF-8");
  
  private static class FieldPrefixStrippingMapper extends MapperWrapper {
    
    public FieldPrefixStrippingMapper(Mapper wrapped) {
      super(wrapped);
    }
    
    private String convert(String fieldName) {
      String result = fieldName;
      if (result.startsWith("f")) {
        result = result.substring(1); // fFoo -> foo
      }
      return result;
    }
    
    @Override
    public String serializedMember(@SuppressWarnings("rawtypes") Class type, String memberName) {
      return super.serializedMember(type, convert(memberName));
    }
    
    @Override
    public String realMember(@SuppressWarnings("rawtypes") Class type, String serialized) {
      String fieldName = super.realMember(type, serialized);
      for (Field field: type.getDeclaredFields()) {
        if (fieldName.equals(convert(field.getName()))) {
          return field.getName();
        }
      }
      return fieldName;
    }
    
  }
  
//  private static class BenchmarkResultEntryConverter implements Converter {
//    
//    @Override
//    public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
//      return BenchmarkResultEntry.class.isAssignableFrom(clazz);
//    }
//    
//    @Override
//    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
//      BenchmarkResultEntry resultMap = (BenchmarkResultEntry)object;
//      context.convertAnother(resultMap.getTask());
//      context.convertAnother(resultMap.getResult());
//    }
//    
//    @Override
//    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
//      BenchmarkResultEntry entry = new BenchmarkResultEntry(null, null);
//      context.convertAnother(entry, BenchmarkTask.class);
//      context.convertAnother(entry, TaskResult.class);
//      return entry;
//    }
//    
//  }
  
  private static class MyCollectionConverter extends CollectionConverter {
    
    public MyCollectionConverter(Mapper mapper) {
      super(mapper);
    }
    
    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      return super.unmarshal(reader, context);
    }
    
  }
  
  private XStream configure() {
    XStream xstream = new XStream(new DomDriver("UTF-8")) {
      
      @Override
      protected MapperWrapper wrapMapper(MapperWrapper next) {
        return new FieldPrefixStrippingMapper(next);
      }
      
      @Override
      protected void setupConverters() {
        super.setupConverters();
        registerConverter(new MyCollectionConverter(getMapper()));
      }
      
    };
    
    
//    xstream.registerConverter(new BenchmarkResultEntryConverter());
    xstream.addImplicitCollection(BenchmarkResult.class, "fResults");
    
//    xstream.addImplicitCollection(TaskResult.class, "fErrors");
//    xstream.addImplicitCollection(TaskResult.class, "fWarnings");
    
    xstream.addImmutableType(ParameterizationValues.class);
    xstream.addImplicitCollection(ParameterizationValues.class, "fValues");
    
    xstream.addImmutableType(BenchmarkTimings.class);
    xstream.addImplicitCollection(BenchmarkTimings.class, "fTimings");
    
    xstream.addImmutableType(Timing.class);
    xstream.addImplicitCollection(GcStats.class, "fGcEvents");
    
    xstream.addImmutableType(BenchmarkParameters.class);
    
    xstream.alias("Benchmark", BenchmarkResult.class);
    xstream.alias("Entry", BenchmarkResultEntry.class);
    xstream.alias("Run", Timing.class);
    
    return xstream;
  }
  
  public void serialize(IBenchmarkResult result, OutputStream out) throws IOException {
    XStream xstream = configure();
    
    OutputStreamWriter writer = new OutputStreamWriter(out, UTF8);
    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
    writer.write(String.format("%n"));
    
    xstream.toXML(result, writer);
    
    out.close();
  }
  
  public IBenchmarkResult deserialize(InputStream byteArrayInputStream) throws IOException {
    XStream xstream = configure();
    Object object = xstream.fromXML(byteArrayInputStream);
    IBenchmarkResult result = ObjectUtil.castOrNull(IBenchmarkResult.class, object);
    if (result == null) {
      throw new IOException("Expected instance of " + IBenchmarkResult.class.getSimpleName() + " but got: " + object);
    }
    return result;
  }
  
}
