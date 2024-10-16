package org.employeemanagement.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.employeemanagement.entities.LeaveRequest;
import org.employeemanagement.repository.interfaces.LeaveRequestRepository;
import org.employeemanagement.utils.JpaUtil;

import java.util.List;

public class LeaveRequestRepositoryImpl implements LeaveRequestRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public LeaveRequest save(LeaveRequest leaveRequest) {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;

        try {
            // Create EntityManager
            entityManager = JpaUtil.getInstance().getEntityManagerFactory().createEntityManager();

            // Start transaction
            transaction = entityManager.getTransaction();
            transaction.begin();

            // Persist the LeaveRequest entity
            entityManager.persist(leaveRequest);
            transaction.commit();
            return leaveRequest;

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // Rollback in case of an error
            }
            throw new RuntimeException("Error saving leave request: " + e.getMessage(), e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close(); // Ensure the EntityManager is closed
            }
        }
    }


    @Override
    @Transactional
    public LeaveRequest update(LeaveRequest leaveRequest) {
        try {
            return entityManager.merge(leaveRequest);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la demande de congé", e);
        }
    }

    @Override
    public LeaveRequest findById(Long id) {
        try {
            LeaveRequest leaveRequest = entityManager.find(LeaveRequest.class, id);
            if (leaveRequest == null) {
                throw new EntityNotFoundException("Demande de congé non trouvée pour l'ID " + id);
            }
            return leaveRequest;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de la demande de congé avec l'ID " + id, e);
        }
    }

    @Override
    public List<LeaveRequest> findAll() {
        try {
            return entityManager.createQuery("SELECT l FROM LeaveRequest l", LeaveRequest.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de la liste des demandes de congé", e);
        }
    }
}


