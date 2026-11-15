
package com.visitor.management.visitor.repository;

import com.visitor.management.visitor.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VisitorRepository extends JpaRepository<Visitor, UUID> {
}
