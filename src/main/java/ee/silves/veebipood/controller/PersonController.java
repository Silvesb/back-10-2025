package ee.silves.veebipood.controller;

import ee.silves.veebipood.entity.Person;
import ee.silves.veebipood.entity.PersonRole;
import ee.silves.veebipood.model.AuthToken;
import ee.silves.veebipood.model.LoginData;
import ee.silves.veebipood.model.PersonDTO;
import ee.silves.veebipood.model.UpdatePassword;
import ee.silves.veebipood.repository.PersonRepository;
import ee.silves.veebipood.service.JwtService;
import ee.silves.veebipood.util.PersonValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonValidator personValidator;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JwtService jwtService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    //ModelMapper modelMapper = new ModelMapper();

    @GetMapping("public-persons")
    public List<PersonDTO> getPublicPersons(){
        return List.of(modelMapper.map(personRepository.findAll(), PersonDTO[].class));
    }

    @PostMapping("signup")
    public Person signup(@RequestBody Person person){
        if (person.getId() != null){
            throw new RuntimeException("Cannot signup with ID");
        }
        if (person.getEmail() == null || !personValidator.validateEmail(person.getEmail())) {
            throw new RuntimeException("Email is not correct");
        }
        personValidator.validatePassword(person.getPassword());

        person.setRole(PersonRole.CUSTOMER);
        person.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));

        return personRepository.save(person);
    }

    @PostMapping("login")
    public AuthToken login(@RequestBody LoginData loginData){
        Person person = personRepository.findByEmail(loginData.getEmail());
        if (person == null) {
            throw new RuntimeException("Invalid email");
        }
        if (!bCryptPasswordEncoder.matches(loginData.getPassword(), person.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtService.generateToken(person);
    }

    @GetMapping("persons")
    public List<Person> getPersons(){
        return personRepository.findAllByOrderByIdAsc();
    }

    @PatchMapping("change-admin")
    public List<Person> changeAdmin(@RequestParam Long personId, boolean isAdmin){
        Person person = personRepository.findById(personId).orElseThrow();
        if (isAdmin){
            person.setRole(PersonRole.ADMIN);
        } else {
            person.setRole(PersonRole.CUSTOMER);
        }
        personRepository.save(person);
        return personRepository.findAllByOrderByIdAsc();
    }

    @GetMapping("person")
    public Person getPerson(){
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return personRepository.findById(id).orElseThrow();
    }

    @PutMapping("update-profile")
    public Person updateProfile(@RequestBody Person person){
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (!person.getId().equals(id)) {
            throw new RuntimeException("Cannot update somebody else's profile");
        }
        Person dbPerson = personRepository.findById(id).orElseThrow();
        person.setRole(dbPerson.getRole());
        person.setPassword(dbPerson.getPassword());

        return personRepository.save(person);
    }

    @PatchMapping("update-password")
    public Person updatePassword(@RequestBody UpdatePassword updatePassword) {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        personValidator.validatePassword(updatePassword.getNewPassword());

        if (!id.equals(updatePassword.getPersonId())) {
            throw new RuntimeException("Cannot update someone else's password");
        }

        Person person = personRepository.findById(id).orElseThrow();

        if (!bCryptPasswordEncoder.matches(updatePassword.getOldPassword(), person.getPassword())) {
            throw new RuntimeException("Old password is not correct");
        }

        person.setPassword(bCryptPasswordEncoder.encode(updatePassword.getNewPassword()));
        return personRepository.save(person);
    }
}
