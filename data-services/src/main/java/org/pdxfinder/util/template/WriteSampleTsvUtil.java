package org.pdxfinder.util.template;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.pdxfinder.constant.FileNames;
import org.pdxfinder.constant.TemplateLocations;
import org.pdxfinder.dto.template.MetadataSampleTsv;
import org.pdxfinder.dto.PdxDto;
import org.pdxfinder.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class WriteSampleTsvUtil {

    private WriteSampleTsvUtil() {
        // Never Called
    }


    public static void writeTsv(List<PdxDto> pdxDtoList, String outputDirectory) throws IOException {

        InputStream contents = FileUtil.class.getResourceAsStream(TemplateLocations.META_DATA_SAMPLE);
        CsvSchema.Builder builder = CsvSchema.builder();
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = builder.build().withHeader().withColumnSeparator('\t');
        MappingIterator<MetadataSampleTsv> iterator = mapper.readerFor(MetadataSampleTsv.class).with(schema).readValues(contents);

        List<MetadataSampleTsv> metadataSampleTsvs = iterator.readAll();
        pdxDtoList.forEach(pdxDto -> metadataSampleTsvs.add(pdxDto.getMetadataSampleTsv()));

        String modelMetaData = FileUtil.serializePojoToTsv(metadataSampleTsvs);
        String output = String.format("%s%s", outputDirectory, FileNames.METADATA_SAMPLE_TSV);
        FileUtil.write(modelMetaData, output);
    }

}