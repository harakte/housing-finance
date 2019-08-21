package com.kakaopay.finance.dao;

import com.kakaopay.finance.entity.Finance;
import com.kakaopay.finance.entity.Institute;
import com.kakaopay.finance.entity.id.FinanceId;
import com.kakaopay.finance.exception.InstituteNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class InstituteFinanceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private FinanceRepository financeRepository;

    @Test
    public void crudTest(){
        List<Institute> inputInstitutes = new ArrayList<>();

        Institute bankA = new Institute("Bank A");
        Finance financeA1 = new Finance(bankA, 2019, 1, 100);
        Finance financeA2 = new Finance(bankA, 2019, 2, 101);
        bankA.addFinance(financeA1);
        bankA.addFinance(financeA2);

        Institute bankB = new Institute("Bank B");
        Finance financeB1 = new Finance(bankB, 2019, 1, 200);
        bankB.addFinance(financeB1);

        inputInstitutes.add(bankA);
        inputInstitutes.add(bankB);

        instituteRepository.saveAll(inputInstitutes);

        Institute bankC = new Institute("Bank C");
        Finance financeC1 = new Finance(bankC, 2019, 1, 300);
        bankC.addFinance(financeC1);
        entityManager.persist(bankC);
        entityManager.persist(financeC1);
        entityManager.flush();

        List<Institute> institutes = instituteRepository.findAll();

        assertThat(institutes)
                .isNotEmpty()
                .hasSize(3)
                .contains(bankA)
                .contains(bankC);

        Institute institute = instituteRepository.findById(bankA.getInstituteCode()).orElseThrow(NoSuchElementException::new);
        assertThat(institute.getFinances())
                .contains(financeA1);

        List<Finance> finances = financeRepository.findAll();

        assertThat(finances)
                .isNotEmpty()
                .hasSize(4)
                .contains(financeA2)
                .contains(financeC1);

        FinanceId financeId = new FinanceId(2019, 1, bankA.getInstituteCode());
        Finance finance = financeRepository.findById(financeId).orElseThrow(NoSuchElementException::new);

        assertThat(finance.getInstitute())
                .isNotNull()
                .isEqualToComparingFieldByFieldRecursively(bankA);

        try {
            Institute findByNameInstitute = instituteRepository.findByInstituteName("Bank A")
                    .orElseThrow(() -> new InstituteNotFoundException("Not fount Bank A"));

            assertThat(findByNameInstitute)
                    .isNotNull()
                    .isEqualToComparingFieldByFieldRecursively(bankA);
        }catch (InstituteNotFoundException ex){

        }
    }
}
