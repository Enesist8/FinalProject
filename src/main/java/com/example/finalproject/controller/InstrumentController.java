package com.example.finalproject.controller;
import com.example.finalproject.model.Instrument;
import com.example.finalproject.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/instruments")
public class InstrumentController {

    private final InstrumentService instrumentService;

    @Autowired
    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @GetMapping
    public String showInstruments(Model model) {
        List<Instrument> instruments = instrumentService.getAllInstruments();
        model.addAttribute("instruments", instruments);
        return "instruments"; //Template name
    }

    @GetMapping("/{id}")
    public String showInstrument(@PathVariable Long id, Model model) {
        Optional<Instrument> instrument = instrumentService.getInstrumentById(id);
        instrument.ifPresent(model::addAttribute);
        return "instrumentDetails"; //Template name
    }

    @GetMapping("/create")
    public String showInstrumentForm(Model model) {
        model.addAttribute("instrument", new Instrument());
        return "createInstrument"; //Template name
    }

    @PostMapping("/create")
    public String createInstrument(@ModelAttribute Instrument instrument, Model model) {
        instrumentService.createInstrument(instrument);
        return "redirect:/instruments";
    }

    @GetMapping("/{id}/edit")
    public String showInstrumentEditForm(@PathVariable Long id, Model model) {
        Optional<Instrument> instrument = instrumentService.getInstrumentById(id);
        instrument.ifPresent(model::addAttribute);
        return "editInstrument"; //Template name
    }

    @PostMapping("/{id}/edit")
    public String updateInstrument(@PathVariable Long id, @ModelAttribute("instrument") Instrument instrument, Model model) {
        try {
            instrumentService.updateInstrument(id, instrument);
            return "redirect:/instruments";
        } catch (Exception e) {
            model.addAttribute("error", "Error updating instrument: " + e.getMessage());
            model.addAttribute("instrument", instrument); // Add the instrument object back to the model
            return "editInstrument";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteInstrument(@PathVariable Long id) {
        instrumentService.deleteInstrument(id);
        return "redirect:/instruments";
    }
}