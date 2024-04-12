package com.webapp.flightsearch.dto;

import com.webapp.flightsearch.entity.JourneyDetails;

import java.util.List;
import java.util.stream.Collectors;

public class JourneyDetailsDto {
    private String duration;
    private String date;
    private List<SegmentDto> segments;

    public JourneyDetailsDto(JourneyDetails journeyDetails) {
        this.duration = journeyDetails.getDuration();
        this.date = journeyDetails.getDate();
        this.segments = journeyDetails.getSegments().stream()
                .map(SegmentDto::new)
                .collect(Collectors.toList());
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<SegmentDto> getSegments() {
        return segments;
    }

    public void setSegments(List<SegmentDto> segments) {
        this.segments = segments;
    }
}
