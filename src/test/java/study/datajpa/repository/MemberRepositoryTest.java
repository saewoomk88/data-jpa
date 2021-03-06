package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;


    @Test
    public void testMember(){
        Member member = new Member("MemberA");
        Member save = memberRepository.save(member);
        Member find = memberRepository.findById(save.getId()).get();

        assertThat(find.getId()).isEqualTo(member.getId());
        assertThat(find.getUsername()).isEqualTo(member.getUsername());
        assertThat(find).isEqualTo(member);

}

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(member1).isEqualTo(findMember1);
        assertThat(member2).isEqualTo(findMember2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long countD = memberRepository.count();
        assertThat(countD).isEqualTo(0);
    }

    @Test
    public void basicCRUD1() {
        Member member1 = new Member("aaa");
        Member save = memberRepository.save(member1);
        List<Member> findMember = memberRepository.findByUsername("aaa");

        assertThat(findMember.get(0)).isEqualTo(save);
    }
    @Test
    public void testList() {
        Member member1 = new Member("aaa");
        Member member2 = new Member("bbb");
        Member save1 = memberRepository.save(member1);
        Member save2 = memberRepository.save(member2);
        List<String> findMember = memberRepository.findUsernameList();
        System.out.println("1111111111111111");


    }

    @Test
    public void testList1() {
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        Member member1 = new Member("aaa");
        member1.setTeam(teamA);
        Member save1 = memberRepository.save(member1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }




    }
    @Test
    public void testList2() {
        Member member1 = new Member("aaa");
        Member member2 = new Member("bbb");
        Member save1 = memberRepository.save(member1);
        Member save2 = memberRepository.save(member2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("aaa", "bbb"));

        for (Member member : result) {
            System.out.println("member = " + member);
        }


    }

    @Test
    public void paging() {

       memberRepository.save(new Member("member1" ,10));
       memberRepository.save(new Member("member2" ,10));
       memberRepository.save(new Member("member3" ,10));
       memberRepository.save(new Member("member4" ,10));
       memberRepository.save(new Member("member5" ,10));

       int age =10;

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        Page<MemberDto> tomap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));


        List<Member> content = page.getContent();
        for (Member member : content) {
            System.out.println("member = " + member);
        }


        assertThat(page.getSize()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0); //현재 페이지 넘버
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue(); //현재가 첫페이지냐?
        assertThat(page.hasNext()).isTrue(); //다음페이지 있냐?


    }

    @Test
    public void entityGraph() {

        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member1", 20));
        memberRepository.save(new Member("member3", 10));

        List<Member> mm = memberRepository.findEntityGraphByUsername("member1");
    }



}