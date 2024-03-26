package io.determann.serialization.xml;

import io.determann.serialization.view.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static io.determann.serialization.view.Properties.*;
import static io.determann.serialization.view.Views.*;
import static io.determann.serialization.xml.Xml.deSerializeXml;
import static io.determann.serialization.xml.Xml.serializeXml;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class XmlTest
{
   @Test
   void serialize()
   {
      PropertySerializationView<Job> jobStructure = propertySerialization(Job.class)
            .with(serialization("id", Long.class, Job::getId))
            .with(serialization("name", String.class, Job::getName));

      PropertySerializationView<Employee> employeeStructure = propertySerialization(Employee.class)
            .with(serialization("id", Long.class, Employee::getId))
            .with(serialization("name", String.class, Employee::getName))
            .with(Properties.serialization("job", Job.class, Employee::getJob, jobStructure));

      Job job = new Job(4L, "firstJob");
      Employee employee = new Employee(1L, "Worker1", job);

      String xml = serializeXml(employeeStructure, employee);
      assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><id>1</id><name>Worker1</name><job><id>4</id><name>firstJob</name></job></root>",
            xml);
   }

   @Test
   void deSerialize()
   {
      PropertyView<Job> jobStructure = property(Job::new)
            .with(readAndWrite("id", Long.class, Job::getId, Job::setId))
            .with(readAndWrite("name", String.class, Job::getName, Job::setName))
            .with(readAndWrite("uuid", String.class, job -> job.getUuid().toString(), (job, s) -> job.setUuid(UUID.fromString(s))));

      PropertyView<Employee> employeeStructure = property(Employee::new)
            .with(readAndWrite("id", Long.class, Employee::getId, Employee::setId))
            .with(readAndWrite("name", String.class, Employee::getName, Employee::setName))
            .with(readAndWrite("job", Job.class, Employee::getJob, Employee::setJob, jobStructure));

      Job job = new Job(4L, "firstJob");
      Employee employee = new Employee(1L, "Worker1", job);

      String xml = serializeXml(employeeStructure, employee);
      assertEquals(employee, deSerializeXml(employeeStructure, xml));

      ConstructorDeSerializationView<Job> constructor = constructor(Job.class)
            .parameter1(Parameters.parameter("id", Long.class))
            .parameter2(Parameters.parameter("name", String.class))
            .constructor(Job::new);

      String jobXml = serializeXml(jobStructure, job);
      assertEquals(job, deSerializeXml(constructor, jobXml));
   }

   class Employee
   {
      private Long id;
      private String name;
      private Job job;

      public Employee()
      {
      }

      public Employee(Long id, String name, Job job)
      {
         this.id = id;
         this.name = name;
         this.job = job;
      }

      public Long getId()
      {
         return id;
      }

      public void setId(Long id)
      {
         this.id = id;
      }

      public String getName()
      {
         return name;
      }

      public void setName(String name)
      {
         this.name = name;
      }

      public Job getJob()
      {
         return job;
      }

      public void setJob(Job job)
      {
         this.job = job;
      }

      @Override
      public boolean equals(Object o)
      {
         if (this == o)
         {
            return true;
         }
         if (!(o instanceof Employee employee))
         {
            return false;
         }

         if (getId() != null ? !getId().equals(employee.getId()) : employee.getId() != null)
         {
            return false;
         }
         if (getName() != null ? !getName().equals(employee.getName()) : employee.getName() != null)
         {
            return false;
         }
         return getJob() != null ? getJob().equals(employee.getJob()) : employee.getJob() == null;
      }

      @Override
      public int hashCode()
      {
         int result = getId() != null ? getId().hashCode() : 0;
         result = 31 * result + (getName() != null ? getName().hashCode() : 0);
         result = 31 * result + (getJob() != null ? getJob().hashCode() : 0);
         return result;
      }
   }
}