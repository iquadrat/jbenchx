package org.jbenchx.result;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class GcStats {
  
  private final List<GcEvent>            fGcEvents       = new ArrayList<GcEvent>();
  
  private transient Map<String, GcEvent> fEventByNameMap = null;
  
  /**
   * Adds a garbage collection event.
   * @param name the name of the collector
   * @param count the number of garbage collections
   * @param time time in ns used for the garbage collections
   */
  public void addGc(String name, long count, long time) {
    GcEvent gcEvent = new GcEvent(name, count, time);
    fGcEvents.add(gcEvent);
    if (fEventByNameMap != null) {
      fEventByNameMap.put(name, gcEvent);
    }
  }
  
  public List<GcEvent> getGcEvents() {
    return Collections.unmodifiableList(fGcEvents);
  }
  
  public boolean noGc() {
    return fGcEvents.isEmpty();
  }
  
  public GcEvent getByName(String name) {
    if (fEventByNameMap == null) {
      buildMap();
    }
    GcEvent gcEvent = fEventByNameMap.get(name);
    if (gcEvent == null) {
      return new GcEvent(name, 0, 0);
    }
    return gcEvent;
  }
  
  private void buildMap() {
    fEventByNameMap = new HashMap<String, GcEvent>(fGcEvents.size());
    for (GcEvent gcEvent: fGcEvents) {
      fEventByNameMap.put(gcEvent.getName(), gcEvent);
    }
  }
  
  @Override
  public String toString() {
    return fGcEvents.toString();
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fGcEvents == null) ? 0 : fGcEvents.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    GcStats other = (GcStats)obj;
    if (fGcEvents == null) {
      if (other.fGcEvents != null) return false;
    } else if (!fGcEvents.equals(other.fGcEvents)) return false;
    return true;
  }
  
  private Object readResolve() throws Exception {
    if (fGcEvents == null) {
      Field field = GcStats.class.getDeclaredField("fGcEvents");
      field.setAccessible(true);
      field.set(this, Collections.emptyList());
    }
    return this;
  }
  
}
