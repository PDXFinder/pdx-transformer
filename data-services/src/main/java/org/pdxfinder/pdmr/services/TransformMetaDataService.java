package org.pdxfinder.pdmr.services;

import org.pdxfinder.constant.DataConstants;
import org.pdxfinder.domain.*;
import org.pdxfinder.dto.*;
import org.pdxfinder.pdmr.dto.OracleDataDto;
import org.pdxfinder.pdmr.extractor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransformMetaDataService {

    private final Logger log = LoggerFactory.getLogger(TransformMetaDataService.class);

    public List<MetadataDto> execute(OracleDataDto dataDto) {

        log.info("Start Transforming metadata data-sets");
        List<String> modelIds = new ArrayList<>();
        List<MetadataDto> metadataDtos = new ArrayList<>();

        for (SpecimenSearch specimenSearch : dataDto.getSpecimenSearchList()) {

            Extract extract = new Extract(specimenSearch, dataDto);

            String modelId = extract.getModelId();
            if (modelId.isEmpty() || modelIds.contains(modelId)) {
                continue;
            }
            modelIds.add(modelId);
            List<TreatmentDto> treatmentDtos = ExtractTreatment.getCurrentTherapies(specimenSearch, dataDto);

            MetadataDto metadataDto = ExtractSpecimenData.get(specimenSearch, dataDto);
            metadataDto.setModelID(modelId)
                    .setPatientID(specimenSearch.getPatientid())
                    .setPrimarySite(specimenSearch.getDiseaselocationdescription())
                    .setSampleType(specimenSearch.getTissuetypedescription())
                    .setInitialDiagnosis("")
                    .setClinicalDiagnosis(extract.getClinicalDiagnosis())
                    .setStageClassification(extract.getStageClassification())
                    .setGradeClassification(extract.getGradeClassification())
                    .setGradeValue(extract.getGradeValue())
                    .setExtractionMethod(extract.getExtractionMethod())
                    .setValidationDtos(extract.getValidations())
                    .setSampleDtos(extract.getSamples())
                    .setStageValue(DataConstants.NOT_SPECIFIED)
                    .setHostStrain(metadataDto.getHostStrainFull().isEmpty() ? DataConstants.EMPTY : DataConstants.NSG_HOST_STRAIN)
                    .setMouseSex(DataConstants.EMPTY)
                    .setTreatmentNaive(DataConstants.EMPTY)
                    .setTreatmentDtos(treatmentDtos)
                    .setSampleState(DataConstants.NOT_SPECIFIED)
                    .setPublications(DataConstants.EMPTY)
                    .setHistory(DataConstants.EMPTY)
                    .setEthnicityAssessmentMethod(DataConstants.EMPTY)
                    .setAgeAtInitialDiagnosis(DataConstants.EMPTY)

                    .setMetadataSampleTsv(extract.sampleTsv(metadataDto))
                    .setMetadataSharingTsv(extract.sharingTsv(metadataDto))
                    .build();

            metadataDtos.add(metadataDto);
        }
        log.info("Finished Transforming metadata data-sets");

        return metadataDtos;
    }

}