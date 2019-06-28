package com.neu.cloudassign1.repository;

import com.neu.cloudassign1.model.CoverImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<CoverImage, UUID> {
}
