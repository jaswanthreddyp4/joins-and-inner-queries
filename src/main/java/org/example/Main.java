package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.dto.CountOfEmployeesInEachTeam;
import org.example.dto.EmployyesFromGenAITeam;
import org.example.entities.Employee;
import org.example.entities.Team;
import org.example.persistence.MyOwnPersistenceInfo;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.w3c.dom.ls.LSOutput;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String,String> propertyMap = new HashMap<>();

        //if you wanna see what queries goes to database
        propertyMap.put("hibernate.show_sql","true");

        EntityManagerFactory emf = new HibernatePersistenceProvider()
                .createContainerEntityManagerFactory(new MyOwnPersistenceInfo(),propertyMap);

        EntityManager em = emf.createEntityManager();

        try{
            em.getTransaction().begin();

//            String jpql1 = "select e from Employee e";

            /*   employees and their team names
            String jpql1 = """
                    select e,e.team.teamName from Employee e
                    """;

             */

            /*
            String jpql1 = """
               SELECT e FROM Employee e LEFT JOIN e.team t WHERE t IS NULL
               """;

             */

            /* team and no of employees in at team
            String jpql1 = """
               SELECT new org.example.dto.CountOfEmployeesInEachTeam(e.team.teamName,count(*)) FROM Employee e group by e.team.id
               """;

             */

            /*
            String jpql1 = """
               select new org.example.dto.EmployyesFromGenAITeam(e.name,e.yearsOfExperience) from Employee e join e.team t where t.teamName='gen ai'
               """;

             */

            String jpql1 = """
              select t from Team t 
              where(
              select avg(e.yearsOfExperience) from Employee e where e.team=t
              )>1
               """;

            TypedQuery<Team> query = em.createQuery(jpql1, Team.class);
            query.getResultList().forEach(System.out::println);

            em.getTransaction().commit();

        }finally {
            em.close();
        }
    }
}