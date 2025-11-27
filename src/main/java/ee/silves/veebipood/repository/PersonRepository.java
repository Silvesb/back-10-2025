package ee.silves.veebipood.repository;

import ee.silves.veebipood.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByEmail(String email);

    List<Person> findAllByOrderByIdAsc();
}
