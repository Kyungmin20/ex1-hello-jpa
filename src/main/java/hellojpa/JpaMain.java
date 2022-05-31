package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        // 애플리케이션에서 1개만 만든다. 서버가 올라오면 1개만 생성된다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 고객의 요청이 올때 마다 작업 한다.
        // 엔티티 매니저는 쓰레드간에 공유 X (사용하고 버려야 한다. )
        EntityManager em = emf.createEntityManager();

        // 주의 JPA의 모든 데이터 변경은 트랜잭션 안에서 실행되어야 한다.
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            //Member 대상이 객체이다  JPQL설명 ---> 방언을 바꾸더라도 설정 가능하다
            List<Member> result =  em.createQuery("select m from Member as m " , Member.class)
                    .setFirstResult(0)
                    .setMaxResults(8)
                    .getResultList();
            // 검색 조건이 포함된 query 실행이 문제
            // JPQL은 엔티티 객체를 대상으로 쿼리
            // SQL은 데이터베이스 데이블을 대상으로 쿼리
            /*
            * JPQL
            * 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
            * SQL을 추상화해서 특정 데이터베이스 SQL에 의존X
            * JPQL을 한마디로 정의하면 객체 지향 SQL
            * */

            
            for (Member member : result) {
                System.out.println("member.getName() = " + member.getName());
            }
            /*h2 데이터 베이스에 데이터 insert
            Member member = new Member();
            member.setId(1L);
            member.setName("HelloB");
            em.persist(member);*/


            /*h2 데이터베이스에 데이터 조회
            Member findMember = em.find(Member.class , 1L);
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());
            */
            /*
            h2 데이터 베이스 삭제
            Member findMember = em.find(Member.class, 1L);
            em.remove(findMember);
            */
            /*
            jpa가 관리 해준다.
            Member findMember = em.find(Member.class , 2L);
            findMember.setName("HelloJPA");
            */


            tx.commit();
        }catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
        }
        //code

        emf.close();

    }



}
