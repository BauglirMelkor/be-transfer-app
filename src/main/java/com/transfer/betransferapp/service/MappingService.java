package com.transfer.betransferapp.service;

import org.mapstruct.Mapper;

import com.transfer.betransferapp.dto.AllowanceAccountDto;
import com.transfer.betransferapp.dto.RestaurantAccountDto;
import com.transfer.betransferapp.entity.AllowanceAccount;
import com.transfer.betransferapp.entity.RestaurantAccount;

@Mapper(componentModel = "spring")
public interface MappingService {

    AllowanceAccountDto allowanceAccountEntityToDTO(AllowanceAccount allowanceAccount);

    RestaurantAccountDto restaurantAccountEntityToDTO(RestaurantAccount restaurantAccount);

    AllowanceAccount allowanceAccountDtoToEntity(AllowanceAccountDto allowanceAccountDto);

    RestaurantAccount restaurantAccountDtotoEntity(RestaurantAccountDto restaurantAccountDto);

}
