package com.melardev.spring.shoppingcartweb.dtos.response.products;


import com.melardev.spring.shoppingcartweb.dtos.response.base.SuccessResponse;
import com.melardev.spring.shoppingcartweb.dtos.response.products.partials.ProductSummaryDto;
import com.melardev.spring.shoppingcartweb.dtos.response.shared.PageMeta;
import com.melardev.spring.shoppingcartweb.models.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ProductListResponse extends SuccessResponse {

    private final PageMeta pageMeta;
    private final List<ProductSummaryDto> products;

    public ProductListResponse(PageMeta pageMeta, List<ProductSummaryDto> productDtos) {
        this.pageMeta = pageMeta;
        this.products = productDtos;
    }

    public static ProductListResponse build(Page<Product> productsPage, String basePath) {
        List<ProductSummaryDto> productDtos = productsPage.getContent().stream()
                .map(ProductSummaryDto::build)
                .collect(Collectors.toList());

        return new ProductListResponse(
                PageMeta.build(productsPage, basePath),
                productDtos
        );
    }

    public PageMeta getPageMeta() {
        return pageMeta;
    }

    public List<ProductSummaryDto> getProducts() {
        return products;
    }
}
