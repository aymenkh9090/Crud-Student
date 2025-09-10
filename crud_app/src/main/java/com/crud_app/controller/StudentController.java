package com.crud_app.controller;

import com.crud_app.model.Student;
import com.crud_app.repository.StudentRepository;
import com.crud_app.service.IStudentService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudentController {
    private final IStudentService studentService;
    private final StudentRepository studentRepository;

    @GetMapping("/")
    public String index(){
        return "layout";
    }

    @GetMapping("/home")
    public String home(){
        return "home";
    }

    /*@GetMapping("/students")
    public String getAllStudent(Model model) {
        List<Student> students = studentService.getAllStudent();
        model.addAttribute("students",students);
        return "students";
    }*/

    // pagination
   /* @GetMapping("/students")
    public String findAllPages(Model model,
                               @RequestParam(name = "page",defaultValue = "0")int page,
                               @RequestParam(name = "size" , defaultValue = "5")int size){
        Page<Student> pageStudents = studentRepository.findAll(PageRequest.of(page,size));
        model.addAttribute("students", pageStudents);
        model.addAttribute("pages",new int[pageStudents.getTotalPages()]); // nombre totale des pages
        model.addAttribute("currentPage",page);
        model.addAttribute("size",size);
        return "students";
    }
*/

    // pagination with search
    @GetMapping("/students")
    public String findAllPages(Model model,
                               @RequestParam(name = "page",defaultValue = "0")int page,
                               @RequestParam(name = "size",defaultValue = "5")int size,
                               @RequestParam(name = "motCle",defaultValue = "")String motCle){
        //Page<Student> pageStudents = studentRepository.findAll(PageRequest.of(page,size));
        Page<Student> pageStudents = studentRepository.findByNameContaining(motCle,PageRequest.of(page,size));
        model.addAttribute("students",pageStudents);
        model.addAttribute("pages",new int[pageStudents.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("size",size);
        model.addAttribute("motCle",motCle);
        return "students";
    }


    @GetMapping("/add")
    public String showRegisterForm(Model model){
        model.addAttribute("student",new Student());
        return "student-form";
    }
    @PostMapping("/add")
    public String addStudent(@Valid @ModelAttribute("student") Student student,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "student-form";
        }
        try {
            studentService.saveStudent(student);
            redirectAttributes.addFlashAttribute("succesMessage","Successfully added student");
            return "redirect:/students";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("erreur","erreur" + e.getMessage());

        }
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id,
                                RedirectAttributes redirectAttributes){
        studentService.deleteStudent(id);
        redirectAttributes.addFlashAttribute("succes","Student deleted Succesfully !!");
        return "redirect:/students";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){
        Student student =
                studentService.getById(id);
        model.addAttribute("student",student);
        return "update-form";
    }

    @PostMapping("/edit")
    public String updateStudent( @ModelAttribute("student") Student student,
                                Long id,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "update-form";
        }
        studentService.updateStudent(student);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/403")
    public String accessDenied(){
        return "403";
    }

    @GetMapping("/generatePdf/{id}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) throws DocumentException, IOException {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article non trouvé pour l'ID: " + id));

        Document document = new Document(); // initailisez nv document vierge
        ByteArrayOutputStream out = new ByteArrayOutputStream(); // stockage dans la flus de memoire
        PdfWriter.getInstance(document, out); // remplissez le document vierge par l'ecriture

        document.open(); // ouvrir le doc

        // style de pdf
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Paragraph title = new Paragraph("PDF N° " + student.getId(), boldFont);
        title.setAlignment(Element.ALIGN_CENTER);
        // ajout sur le document
        document.add(title);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Détails de l'Etudiant"));
        document.add(new Paragraph("Name : " + student.getName()));
        document.add(new Paragraph("Email : " + student.getEmail()));
        document.add(new Paragraph("Adress : " + student.getAdress()));
        // fermer la doc
        document.close();
        // retour http
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "details_" + student.getId() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(out.toByteArray());
    }



}
