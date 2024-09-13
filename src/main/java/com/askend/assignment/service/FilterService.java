package com.askend.assignment.service;

import com.askend.assignment.model.Filter;
import com.askend.assignment.model.FilterCriteria;
import com.askend.assignment.repository.FilterCriteriaRepository;
import com.askend.assignment.repository.FilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilterService {

    @Autowired
    private FilterRepository filterRepository;
    @Autowired
    private FilterCriteriaRepository filterCriteriaRepository;

    public ResponseEntity<List<Filter>> getAllFilters() {
        List<Filter> filters = filterRepository.findAll();
        if (filters.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(filters, HttpStatus.OK);
    }

    public ResponseEntity<Filter> getFilterById(Long id) {
        Optional<Filter> filter = filterRepository.findById(id);
        if (filter.isPresent()) {
            return new ResponseEntity<>(filter.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Filter> createFilter(Filter filter) {
        try {
            Filter createdFilter = filterRepository.save(filter);
            return new ResponseEntity<>(createdFilter, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Filter> deleteCriteria(Long filterId, Long criteriaId) {
        Optional<Filter> filter = filterRepository.findById(filterId);
        if (filter.isPresent()) {
            Optional<FilterCriteria> criteria = filterCriteriaRepository.findById(criteriaId);

            if (criteria.isPresent()) {
                filterCriteriaRepository.delete(criteria.get());
                return new ResponseEntity<>(filter.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Filter> addCriteria(Long filterId, FilterCriteria newCriteria) {
        Optional<Filter> filter = filterRepository.findById(filterId);

        if (filter.isPresent()) {
            Filter currentFilter = filter.get();
            newCriteria.setFilter(currentFilter);
            currentFilter.getCriteria().add(newCriteria);

            Filter updatedFilter = filterRepository.save(currentFilter);
            return new ResponseEntity<>(updatedFilter, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
