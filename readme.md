# Current State

Serialization and DeSerialization is everywhere.
If it's based on reflection it has a lot of problems


From [Why We Hate Java Serialization And What We're Doing About It by Brian Goetz & Stuart Marks](https://www.youtube.com/watch?v=dOgfWXw9VrI)

> - Undermines encapsulation in non-obvious ways
> - Hard to evolve serializable classes
> - Too hard to reason about security
> - Ongoing tax on evolving the language
> - Cannot verify correctness by reading the code (!)  

A lot of the Points also apply to any reflection based serialization.

# Proposal
Uses Views on the Model for Serialization/DeSerialization instead of the Model or reflection.
Views can be built with builders or generated with an annotation-processor.
They have a one-to-many relation with the model allowing for different Serializations.
If build by hand and not generated the views are completely decoupled from the Model.


For a model like

````java
import java.util.UUID;

@Serializable//only triggers code generation
public class Job {
   private Long id;
   @Attribute//only triggers code generation
   private String name;
   private UUID uuid;

   //getter setter etc
}
````

This would be generated. 
Code generation is only vor convince and can be used as a starting point to build custom views like one that does not contain all properties or inlines child properties.
As long as the signature conforms to getter/setter it can be used for property based serialization.
constructor deserialization is supported as well.

````java
public interface JobView {

public static final ReadAndWriteProperty<Job, Long, ?> ID = Properties.readAndWrite("id", Long.class, Job::getId, Job::setId);

public static final ReadAndWriteProperty<Job, String, ?> NAME = Properties.readAndWrite("name", String.class, Job::getName, Job::setName)
                                                                          .withSetting(Xml.attribute());

public static final ReadAndWriteProperty<Job, String, ?> UUID = Properties.readAndWrite("uuid", 
                                                                                        String.class, 
                                                                                        job -> job.getUuid().toString(), 
                                                                                        (job, uuid) -> job.setUuid(java.util.UUID.fromString(uuid)));

public static final PropertyView<Job> JOB_PROPERTY_VIEW = Views.property(Job::new).with(ID).with(NAME).with(UUID);
}
````

Views are independent of the serialization target. 
The same view can be used for xml, json, csv etc.

````java
Job job = create(Job.class);

String xml = Xml.serializeXml(JobView.JOB_PROPERTY_VIEW, job);

assertEquals(job, Xml.deSerializeXml(JobView.JOB_PROPERTY_VIEW, xml));
````

## This is just a proof of concept

Open todos
- one to many