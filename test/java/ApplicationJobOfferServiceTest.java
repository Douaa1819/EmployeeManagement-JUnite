import org.employeemanagement.entities.ApplicationJobOffer;
import org.employeemanagement.repository.interfaces.ApplicationJobOfferRepository;
import org.employeemanagement.service.ApplicationJobOfferServiceImpl;
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
 * Unit tests for the ApplicationJobOfferServiceImpl class.
 */
public class ApplicationJobOfferServiceTest {

    @Mock
    private ApplicationJobOfferRepository applicationJobOfferRepository;

    @InjectMocks
    private ApplicationJobOfferServiceImpl applicationJobOfferService;

    /**
     * Initializes mocks before each test case.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the save method of the ApplicationJobOfferService.
     * It verifies that an application job offer is saved correctly.
     */
    @Test
    public void testSaveApplicationJobOffer() {
        ApplicationJobOffer applicationJobOffer = new ApplicationJobOffer();
        applicationJobOffer.setId(1L);

        doNothing().when(applicationJobOfferRepository).save(any(ApplicationJobOffer.class));

        applicationJobOfferService.save(applicationJobOffer);

        verify(applicationJobOfferRepository, times(1)).save(applicationJobOffer);
    }

    /**
     * Tests the findAll method of the ApplicationJobOfferService.
     * It verifies that all application job offers are retrieved correctly.
     */
    @Test
    public void testFindAllApplicationJobOffers() {
        List<ApplicationJobOffer> applicationJobOffers = new ArrayList<>();
        applicationJobOffers.add(new ApplicationJobOffer());
        applicationJobOffers.add(new ApplicationJobOffer());

        when(applicationJobOfferRepository.findAll()).thenReturn(applicationJobOffers);

        List<ApplicationJobOffer> foundApplicationJobOffers = applicationJobOfferService.findAll();

        assertNotNull(foundApplicationJobOffers);
        assertEquals(2, foundApplicationJobOffers.size());
        verify(applicationJobOfferRepository, times(1)).findAll();
    }

    /**
     * Tests the findById method of the ApplicationJobOfferService when the job offer exists.
     * It verifies that the application job offer is found correctly.
     */
    @Test
    public void testFindByIdFound() {
        ApplicationJobOffer applicationJobOffer = new ApplicationJobOffer();
        applicationJobOffer.setId(1L);

        when(applicationJobOfferRepository.findById(1L)).thenReturn(applicationJobOffer);

        ApplicationJobOffer foundApplicationJobOffer = applicationJobOfferService.findById(1L);

        assertNotNull(foundApplicationJobOffer);
        assertEquals(1L, foundApplicationJobOffer.getId());
        verify(applicationJobOfferRepository, times(1)).findById(1L);
    }

    /**
     * Tests the findById method of the ApplicationJobOfferService when the job offer does not exist.
     * It verifies that null is returned.
     */
    @Test
    public void testFindByIdNotFound() {
        when(applicationJobOfferRepository.findById(999L)).thenReturn(null);

        ApplicationJobOffer foundApplicationJobOffer = applicationJobOfferService.findById(999L);

        assertNull(foundApplicationJobOffer);
        verify(applicationJobOfferRepository, times(1)).findById(999L);
    }

    /**
     * Tests the findByStatus method of the ApplicationJobOfferService.
     * It verifies that application job offers are retrieved by their status correctly.
     */
    @Test
    public void testFindByStatus() {
        List<ApplicationJobOffer> applicationJobOffers = new ArrayList<>();
        applicationJobOffers.add(new ApplicationJobOffer());
        applicationJobOffers.add(new ApplicationJobOffer());

        when(applicationJobOfferRepository.findByStatus(true)).thenReturn(applicationJobOffers);

        List<ApplicationJobOffer> foundApplicationJobOffers = applicationJobOfferService.findByStatus(true);

        assertNotNull(foundApplicationJobOffers);
        assertEquals(2, foundApplicationJobOffers.size());
        verify(applicationJobOfferRepository, times(1)).findByStatus(true);
    }

    /**
     * Tests the update method of the ApplicationJobOfferService.
     * It verifies that an application job offer is updated correctly.
     */
    @Test
    public void testUpdateApplicationJobOffer() {
        ApplicationJobOffer applicationJobOffer = new ApplicationJobOffer();
        applicationJobOffer.setId(1L);

        doNothing().when(applicationJobOfferRepository).update(any(ApplicationJobOffer.class));

        applicationJobOfferService.update(applicationJobOffer);

        verify(applicationJobOfferRepository, times(1)).update(applicationJobOffer);
    }

    /**
     * Tests the acceptApplication method of the ApplicationJobOfferService.
     * It verifies that an application is accepted correctly.
     */
    @Test
    public void testAcceptApplication() {
        Long applicationId = 1L;
        Long jobOfferId = 2L;

        doNothing().when(applicationJobOfferRepository).acceptApplication(applicationId, jobOfferId);

        applicationJobOfferService.acceptApplication(applicationId, jobOfferId);

        verify(applicationJobOfferRepository, times(1)).acceptApplication(applicationId, jobOfferId);
    }

    /**
     * Tests the findByJobOfferIdAndApplicationId method of the ApplicationJobOfferService.
     * It verifies that the application job offer is found correctly based on the job offer and application IDs.
     */
    @Test
    public void testFindByJobOfferIdAndApplicationIdFound() {
        ApplicationJobOffer applicationJobOffer = new ApplicationJobOffer();
        applicationJobOffer.setId(1L);

        when(applicationJobOfferRepository.findByApplicationIdAndJobOfferId(2L, 1L)).thenReturn(applicationJobOffer);

        ApplicationJobOffer foundApplicationJobOffer = applicationJobOfferService.findByJobOfferIdAndApplicationId(2L, 1L);

        assertNotNull(foundApplicationJobOffer);
        assertEquals(1L, foundApplicationJobOffer.getId());
        verify(applicationJobOfferRepository, times(1)).findByApplicationIdAndJobOfferId(2L, 1L);
    }

    /**
     * Tests the findByJobOfferIdAndApplicationId method of the ApplicationJobOfferService
     * when the application job offer does not exist.
     * It verifies that null is returned.
     */
    @Test
    public void testFindByJobOfferIdAndApplicationIdNotFound() {
        when(applicationJobOfferRepository.findByApplicationIdAndJobOfferId(999L, 888L)).thenReturn(null);

        ApplicationJobOffer foundApplicationJobOffer = applicationJobOfferService.findByJobOfferIdAndApplicationId(999L, 888L);

        assertNull(foundApplicationJobOffer);
        verify(applicationJobOfferRepository, times(1)).findByApplicationIdAndJobOfferId(999L, 888L);
    }
}
