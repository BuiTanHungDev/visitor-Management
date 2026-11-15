
package com.visitor.management.soldier.repository;

import com.visitor.management.soldier.entity.Soldier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SoldierRepository extends JpaRepository<Soldier, UUID> {
}
