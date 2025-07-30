# gen_mapper.py
import os

entity_list = ['LoginHistory']
package = 'com.flinksight.backend.mapper'

for name in entity_list:
    code = f'''package {package};

import org.mapstruct.Mapper;
import {package.replace('.mapper', '.domain')}.{name};
import com.flinksight.common.dto.{name}DTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface {name}StructMapper extends GenericMapper<{name}DTO, {name}> {{}}
'''
    with open(f'{name}StructMapper.java', 'w', encoding='utf-8') as f:
        f.write(code)
