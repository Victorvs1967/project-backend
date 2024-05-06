package com.vvs.backend.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AppMapper {

    private ModelMapper mapper = new ModelMapper();

    public <T, R> R convert(T item, Class<R> typeParameterClass) {
        return mapper.map(item, typeParameterClass);
    }

    public <T, R> List<R> convertToList(List<T> list, Class<R> typeParameterClass) {
        return list.stream()
            .map(item -> mapper.map(item, typeParameterClass))
            .collect(Collectors.toList());
    }
}
