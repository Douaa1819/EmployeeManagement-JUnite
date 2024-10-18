import org.employeemanagement.entities.Application;
import org.employeemanagement.repository.interfaces.ApplicationRepository;
import org.employeemanagement.service.ApplicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ApplicationServiceImpl class.
 */
public class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    /**
     * Initializes mocks before each test case.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the save method of the ApplicationService.
     * It verifies that the application is saved correctly.
     */
    @Test
    public void testSaveApplication() {
        Application application = new Application();
        application.setId(1L);

        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        Application savedApplication = applicationService.save(application);

        assertNotNull(savedApplication);
        assertEquals(1L, savedApplication.getId());
        verify(applicationRepository, times(1)).save(application);
    }

    /**
     * Tests the findAll method of the ApplicationService.
     * It verifies that all applications are retrieved correctly.
     */
    @Test
    public void testFindAllApplications() {
        List<Application> applications = new ArrayList<>();
        applications.add(new Application());
        applications.add(new Application());

        when(applicationRepository.findAll()).thenReturn(applications);

        List<Application> foundApplications = applicationService.findAll();

        assertNotNull(foundApplications);
        assertEquals(2, foundApplications.size());
        verify(applicationRepository, times(1)).findAll();
    }

    /**
     * Tests the findAllSkills method of the ApplicationService.
     * It verifies that all unique skills are retrieved correctly.
     */
    @Test
    public void testFindAllSkills() {
        List<String> skills = new ArrayList<>();
        skills.add("Java");
        skills.add("Python");
        skills.add("JavaScript");

        when(applicationRepository.findAllSkills()).thenReturn(skills);

        List<String> foundSkills = applicationService.findAllSkills();

        assertNotNull(foundSkills);
        assertEquals(3, foundSkills.size());
        assertTrue(foundSkills.contains("Java"));
        verify(applicationRepository, times(1)).findAllSkills();
    }

    /**
     * Tests the findById method of the ApplicationService when the application exists.
     * It verifies that the application is found correctly.
     */
    @Test
    public void testFindByIdFound() {
        Application application = new Application();
        application.setId(1L);

        when(applicationRepository.findById(1L)).thenReturn(application);

        Application foundApplication = applicationService.findById(1L);

        assertNotNull(foundApplication);
        assertEquals(1L, foundApplication.getId());
        verify(applicationRepository, times(1)).findById(1L);
    }

    /**
     * Tests the findById method of the ApplicationService when the application does not exist.
     * It verifies that null is returned.
     */
    @Test
    public void testFindByIdNotFound() {
        when(applicationRepository.findById(999L)).thenReturn(null);

        Application foundApplication = applicationService.findById(999L);

        assertNull(foundApplication);
        verify(applicationRepository, times(1)).findById(999L);
    }

    /**
     * Tests the findDistinctByCompetencies method of the ApplicationService.
     * It verifies that distinct applications are found based on the given competency.
     */
    @Test
    public void testFindDistinctByCompetencies() {
        List<Application> applications = new ArrayList<>();
        applications.add(new Application());
        applications.add(new Application());

        when(applicationRepository.findDistinctByCompetencies("Java")).thenReturn(applications);

        List<Application> foundApplications = applicationService.findDistinctByCompetencies("Java");

        assertNotNull(foundApplications);
        assertEquals(2, foundApplications.size());
        verify(applicationRepository, times(1)).findDistinctByCompetencies("Java");
    }

    /**
     * Tests the findDistinctByCompetencies method of the ApplicationService when no applications are found.
     * It verifies that an empty list is returned.
     */
    @Test
    public void testFindDistinctByCompetenciesNotFound() {
        when(applicationRepository.findDistinctByCompetencies("Unknown")).thenReturn(new ArrayList<>());

        List<Application> foundApplications = applicationService.findDistinctByCompetencies("Unknown");

        assertNotNull(foundApplications);
        assertTrue(foundApplications.isEmpty());
        verify(applicationRepository, times(1)).findDistinctByCompetencies("Unknown");
    }
}
