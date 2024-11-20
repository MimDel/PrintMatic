package com.example.printmatic.dto.request;

import com.example.printmatic.enums.PageSize;
import com.example.printmatic.enums.PaperType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationDTO {

    @NotNull(message = "The title has to be present")
    private String title;

    @NotNull(message = "Number of copies can't be null")
    @Min(value = 1, message = "At least one copy is required.")
    private Integer copies;

    @NotNull(message = "Color mode must be specified.")
    private boolean grayscale;

    @NotNull(message = "Page size is required.")
    private PageSize pageSize;

    @NotNull(message = "Paper type is required.")
    private PaperType paperType;

    private String additionalInfo;

    private LocalDateTime deadline;

    private String fileUrl;
}
