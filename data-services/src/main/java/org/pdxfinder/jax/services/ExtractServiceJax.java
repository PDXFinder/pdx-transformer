package org.pdxfinder.jax.services;

import org.pdxfinder.dto.MetadataDto;
import org.pdxfinder.dto.SampleDto;
import org.pdxfinder.jax.dto.JaxDataDto;
import org.pdxfinder.jax.extractor.ExtractJax;
import org.pdxfinder.result.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.pdxfinder.constant.*;
import org.springframework.stereotype.Service;

@Service
public class ExtractServiceJax {

    private final Logger log = LoggerFactory.getLogger(ExtractServiceJax.class);
    private DataServiceJax dataServiceJax;

    public ExtractServiceJax(DataServiceJax dataServiceJax) {
        this.dataServiceJax = dataServiceJax;
    }

    public JaxDataDto fromJAXFeed() throws IOException {

        log.info("Start Loading JAX data");
        List<MutationTsv> fullMutationData = new ArrayList<>();
        List<CnaTsv> fullCnaData = new ArrayList<>();
        List<ExpressionTsv> fullExpressionData = new ArrayList<>();

        List<MetadataDto> metadataDtoList = dataServiceJax.getAllMetadata();
        for (MetadataDto metadataDto : metadataDtoList) {

            String modelId = metadataDto.getModelID();
            List<MutationTsv> mutationTsvList = dataServiceJax.getAllMutationData(modelId);
            List<CnaTsv> cnaTsvList = dataServiceJax.getAllCopyNumberAlterationData(modelId);
            List<ExpressionTsv> expressionTsvList = dataServiceJax.getAllExpressionData(modelId);

            List<SampleDto> sampleDtos = ExtractJax.getSamplesFromGenomicData(mutationTsvList);
            metadataDto.setSampleDtos(sampleDtos)
                    .setHostStrainFull(DataConstants.NSG_HOST_STRAIN_FULL)
                    .setSampleState(DataConstants.NOT_SPECIFIED)
                    .setPublications(DataConstants.EMPTY)
                    .setHistory(DataConstants.EMPTY)
                    .setEthnicityAssessmentMethod(DataConstants.EMPTY)
                    .setAgeAtInitialDiagnosis(DataConstants.EMPTY)
                    .setValidationDtos(ExtractJax.getValidations())
                    .setMetadataSampleTsv(ExtractJax.sampleTsv(metadataDto))
                    .setMetadataSharingTsv(ExtractJax.sharingTsv(metadataDto));

            fullExpressionData.addAll(expressionTsvList);
            fullCnaData.addAll(cnaTsvList);
            fullMutationData.addAll(mutationTsvList);

            dataServiceJax.getAllHistologyData(modelId);
        }

        log.info("Finished Loading JAX data");
        return new JaxDataDto()
                .setMetadataDtos(metadataDtoList)
                .setMutationTsvs(fullMutationData)
                .setCnaTsvs(fullCnaData)
                .setExpressionTsvs(fullExpressionData)
                .build();
    }

}