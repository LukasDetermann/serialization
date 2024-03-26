package io.determann.serialization.xml;


import java.util.UUID;

public class Job
{
   private Long id;
   @Attribute
   private String name;
   private UUID uuid;

   public Job()
   {
   }

   public Job(Long id, String name)
   {
      this.id = id;
      this.name = name;
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

   public UUID getUuid()
   {
      return uuid;
   }

   public void setUuid(UUID uuid)
   {
      this.uuid = uuid;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (!(o instanceof Job job))
      {
         return false;
      }

      if (getId() != null ? !getId().equals(job.getId()) : job.getId() != null)
      {
         return false;
      }
      return getName() != null ? getName().equals(job.getName()) : job.getName() == null;
   }

   @Override
   public int hashCode()
   {
      int result = getId() != null ? getId().hashCode() : 0;
      result = 31 * result + (getName() != null ? getName().hashCode() : 0);
      return result;
   }
}
