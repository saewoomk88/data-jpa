package study.datajpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember(){
        Member member = new Member("MemberA");
        Member save = memberJpaRepository.save(member);
        Member find = memberJpaRepository.find(save.getId());

        assertThat(find.getId()).isEqualTo(member.getId());
        assertThat(find.getUsername()).isEqualTo(member.getUsername());
        assertThat(find).isEqualTo(member);

    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        assertThat(member1).isEqualTo(findMember1);
        assertThat(member2).isEqualTo(findMember2);

        //리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long countD = memberJpaRepository.count();
        assertThat(countD).isEqualTo(0);
    }
    @Test
    public void bulkUpdate(){
        memberJpaRepository.save(new Member("member1" ,10));
        memberJpaRepository.save(new Member("member2" ,19));
        memberJpaRepository.save(new Member("member3" ,20));
        memberJpaRepository.save(new Member("member4" ,21));
        memberJpaRepository.save(new Member("member5" ,40));

        int result = memberJpaRepository.bulkAgePlus(20);

        assertThat(result).isEqualTo(3);


    }


}