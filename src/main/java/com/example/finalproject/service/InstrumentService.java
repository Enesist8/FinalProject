package com.example.finalproject.service;

import com.example.finalproject.model.Instrument;
import com.example.finalproject.repository.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;

    @Autowired
    public InstrumentService(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }
    public Optional<Instrument> findById(Long id) {
        return instrumentRepository.findById(id);
    }
    public List<Instrument> findAll() {
        return instrumentRepository.findAll();
    }

    public List<Instrument> getAllInstruments() {
        return instrumentRepository.findAll();
    }

    public Optional<Instrument> getInstrumentById(Long id) {
        return instrumentRepository.findById(id);
    }

    public Instrument createInstrument(Instrument instrument) {
        return instrumentRepository.save(instrument);
    }

    public Instrument updateInstrument(Long id, Instrument updatedInstrument) {
        return instrumentRepository.findById(id)
                .map(instrument -> {
                    instrument.setName(updatedInstrument.getName());
                    instrument.setDescription(updatedInstrument.getDescription());
                    instrument.setBrand(updatedInstrument.getBrand());
                    instrument.setType(updatedInstrument.getType());
                    instrument.setPrice(updatedInstrument.getPrice());
                    return instrumentRepository.save(instrument);
                })
                .orElseThrow(() -> new RuntimeException("Instrument not found with ID: " + id));
    }

    public void deleteInstrument(Long id) {
        instrumentRepository.deleteById(id);
    }
}