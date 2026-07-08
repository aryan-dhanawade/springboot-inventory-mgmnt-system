package com.aryan.inventory.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

import com.aryan.inventory.dto.ProductFilter;
import com.aryan.inventory.entity.Product;

public class ProductSpecification {

	public static Specification<Product> filter(ProductFilter filter) {

		return (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<>();

			if (filter.getName() != null && !filter.getName().isBlank()) {
				predicates.add(cb.like(

						cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"

				)

				);
			}

			if (filter.getCategory() != null && !filter.getCategory().isBlank()) {

				predicates.add(

						cb.like(cb.lower(root.get("category").get("name")), filter.getCategory().toLowerCase())

				);
			}

			if (filter.getSupplier() != null && !filter.getSupplier().isBlank()) {

				predicates.add(

						cb.like(cb.lower(root.get("supplier").get("name")), filter.getSupplier().toLowerCase())

				);
			}

			if (filter.getMinPrice() != null) {

				predicates.add(

						cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice())

				);
			}

			if (filter.getMaxPrice() != null) {

				predicates.add(

						cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice())

				);
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
