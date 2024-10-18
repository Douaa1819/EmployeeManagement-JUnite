import org.employeemanagement.entities.JobOffer;
import org.employeemanagement.exception.InvalidInputException;
import org.employeemanagement.repository.interfaces.JobOffreRepository;
import org.employeemanagement.service.JobOffreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JobOfferTest {

    @Mock
    private JobOffreRepository jobOfferRepository;

    @InjectMocks
    private JobOffreServiceImpl jobOffreService;

    private JobOffer jobOffer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jobOffer = new JobOffer("Software Engineer", "Develop software applications", new Date(), true);
    }

    /**
     * Test the successful addition of a job offer.
     */
    @Test
    public void testAddJobOffer() {
        when(jobOfferRepository.save(any(JobOffer.class))).thenReturn(jobOffer);

        JobOffer createdJobOffer = jobOffreService.addJobOffer(jobOffer);

        assertEquals(jobOffer.getTitle(), createdJobOffer.getTitle());
        verify(jobOfferRepository, times(1)).save(any(JobOffer.class));
    }

    /**
     * Test adding a job offer with null title to check for InvalidInputException.
     */
    @Test
    public void testAddJobOffer_NullTitle() {
        jobOffer.setTitle(null);
        assertThrows(InvalidInputException.class, () -> jobOffreService.addJobOffer(jobOffer));
    }

    /**
     * Test adding a job offer with empty description to check for InvalidInputException.
     */
    @Test
    public void testAddJobOffer_EmptyDescription() {
        jobOffer.setDescription("");
        assertThrows(InvalidInputException.class, () -> jobOffreService.addJobOffer(jobOffer));
    }

    /**
     * Test fetching a job offer by valid ID.
     */
    @Test
    public void testGetJobOfferById() {
        when(jobOfferRepository.findById(1L)).thenReturn(jobOffer);

        JobOffer foundJobOffer = jobOffreService.getJobOfferById(1L);

        assertEquals(jobOffer.getTitle(), foundJobOffer.getTitle());
        verify(jobOfferRepository, times(1)).findById(1L);
    }

    /**
     * Test fetching a job offer by an invalid ID to check for InvalidInputException.
     */
    @Test
    public void testGetJobOfferById_NotFound() {
        when(jobOfferRepository.findById(1L)).thenReturn(null);
        assertThrows(InvalidInputException.class, () -> jobOffreService.getJobOfferById(1L));
    }

    /**
     * Test retrieving all job offers.
     */
    @Test
    public void testGetAllJobOffers() {
        List<JobOffer> jobOffers = new ArrayList<>();
        jobOffers.add(jobOffer);
        when(jobOfferRepository.findAll()).thenReturn(jobOffers);

        List<JobOffer> foundJobOffers = jobOffreService.getAllJobOffers();

        assertEquals(jobOffers.size(), foundJobOffers.size());
        verify(jobOfferRepository, times(1)).findAll();
    }

    /**
     * Test updating an existing job offer.
     */
    @Test
    public void testUpdateJobOffer() {
        // Set up the existing job offer
        JobOffer existingJobOffer = new JobOffer();
        existingJobOffer.setId(1L);
        existingJobOffer.setTitle("Software Engineer");
        existingJobOffer.setDescription("Develop software applications");
        existingJobOffer.setPostDate(new Date());
        existingJobOffer.setStatus(true);

        when(jobOfferRepository.findById(1L)).thenReturn(existingJobOffer);
        when(jobOfferRepository.update(any(JobOffer.class))).thenReturn(existingJobOffer);

        // Update the job offer
        existingJobOffer.setTitle("Senior Software Engineer");
        JobOffer updatedJobOffer = jobOffreService.updateJobOffer(existingJobOffer);

        // Check that the title was updated
        assertEquals("Senior Software Engineer", updatedJobOffer.getTitle());
        verify(jobOfferRepository, times(1)).update(existingJobOffer);
    }

    /**
     * Test updating a non-existing job offer to check for InvalidInputException.
     */
    @Test
    public void testUpdateJobOffer_NotFound() {
        JobOffer jobOfferToUpdate = new JobOffer();
        jobOfferToUpdate.setId(1L);  // Non-existing ID

        when(jobOfferRepository.findById(1L)).thenReturn(null);
        assertThrows(InvalidInputException.class, () -> jobOffreService.updateJobOffer(jobOfferToUpdate));
    }

    /**
     * Test deleting a job offer.
     */
    @Test
    public void testDeleteJobOffer() {
        JobOffer jobOfferToDelete = new JobOffer();
        jobOfferToDelete.setId(1L);
        when(jobOfferRepository.findById(1L)).thenReturn(jobOfferToDelete);
        doNothing().when(jobOfferRepository).delete(1L);

        jobOffreService.deleteJobOffer(1L);

        verify(jobOfferRepository, times(1)).delete(1L);
    }

    /**
     * Test deleting a non-existing job offer to check for InvalidInputException.
     */
    @Test
    public void testDeleteJobOffer_NotFound() {
        when(jobOfferRepository.findById(1L)).thenReturn(null);
        assertThrows(InvalidInputException.class, () -> jobOffreService.deleteJobOffer(1L));
    }
}
