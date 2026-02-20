package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Facility;
import com.consultrix.consultrixserver.model.Organization;
import com.consultrix.consultrixserver.repository.FacilityRepository;
import com.consultrix.consultrixserver.repository.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final OrganizationRepository organizationRepository;

    public FacilityService(
            FacilityRepository FacilityRepository,
            OrganizationRepository organizationRepository
    ) {
        this.facilityRepository = FacilityRepository;
        this.organizationRepository = organizationRepository;
    }

    // create Facility
    public Facility create(
            Integer organizationId, String name, String addressLine1,
            String city, String state, String country,
            Integer capacity, LocalDate leaseStartDate, LocalDate leaseEndDate
    ) {
        if (organizationId == null) {
            throw new IllegalArgumentException("organizationId are required");
        }

        List<Facility> existing = facilityRepository.findByOrganizationId(organizationId);
        if(existing != null) {
            throw new IllegalArgumentException("Facility already exists!");
        }

        Organization org = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found: " + organizationId));

        Facility facility = new Facility();
        facility.setOrganization(org);
        facility.setName(name);
        facility.setAddress_line1(addressLine1);
        facility.setCity(city);
        facility.setState(state);
        facility.setCountry(country);
        facility.setCapacity(capacity);
        facility.setLeaseStartDate(leaseStartDate);
        facility.setLeaseEndDate(leaseEndDate);

        return facilityRepository.save(facility);
    }

    // getAll
    @Transactional(readOnly = true)
    public List<Facility> listAll() {
        return facilityRepository.findAll();
    }

    // getByStatus
    @Transactional(readOnly = true)
    public List<Facility> listByStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("studentId is required");
        }
        return facilityRepository.findByStatus(status);
    }

    // getById
    @Transactional(readOnly = true)
    public Facility getById(Integer facilityId) {
        if (facilityId == null) {
            throw new IllegalArgumentException("FacilityId is required");
        }
        return facilityRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("Facility not found: " + facilityId));
    }

    // update Facility
    public Facility update(Integer FacilityId, Facility facility) {
        Facility existing = getById(FacilityId);

        existing.setCity(facility.getCity());
        existing.setStatus(facility.getStatus());
        existing.setState(facility.getState());
        existing.setCountry(facility.getCountry());
        existing.setCapacity(facility.getCapacity());
        existing.setName(facility.getName());
        existing.setAddress_line1(facility.getAddress_line1());

        return facilityRepository.save(existing);
    }

    // delete Facility
    public void delete(Integer facilityId) {
        if (facilityId == null) {
            throw new IllegalArgumentException("FacilityId is required");
        }
        if (!facilityRepository.existsById(facilityId)) {
            throw new IllegalArgumentException("Facility not found: " + facilityId);
        }
        facilityRepository.deleteById(facilityId);
    }
}

