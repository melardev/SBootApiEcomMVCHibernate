package com.melardev.spring.shoppingcartweb.dtos.response.addresses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.melardev.spring.shoppingcartweb.dtos.response.base.SuccessResponse;
import com.melardev.spring.shoppingcartweb.models.Address;

import java.util.ArrayList;
import java.util.List;

public class AddressListDto extends SuccessResponse {
    @JsonProperty("addresses")
    private final List<AddressExcludeUserDto> addressDtos;

    public AddressListDto(List<AddressExcludeUserDto> addressDtos) {
        this.addressDtos = addressDtos;
    }

    public static AddressListDto build(List<Address> addresses) {
        List<AddressExcludeUserDto> addressDtos = new ArrayList<>(addresses.size());
        addresses.forEach(a -> addressDtos.add(AddressExcludeUserDto.build(a)));
        return new AddressListDto(addressDtos);
    }

    public List<AddressExcludeUserDto> getAddressDtos() {
        return addressDtos;
    }
}
