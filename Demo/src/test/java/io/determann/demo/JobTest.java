package io.determann.demo;

import io.determann.serialization.xml.Xml;
import org.junit.jupiter.api.Test;

import static org.instancio.Instancio.create;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JobTest
{
   @Test
   public void demo()
   {
      Job job = create(Job.class);

      String xml = Xml.serializeXml(JobView.JOB_PROPERTY_VIEW, job);

      assertEquals(job, Xml.deSerializeXml(JobView.JOB_PROPERTY_VIEW, xml));
   }
}