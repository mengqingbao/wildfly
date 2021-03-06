/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.clustering.infinispan.subsystem;

import org.infinispan.AdvancedCache;
import org.infinispan.Cache;
import org.jboss.as.clustering.controller.Operation;
import org.jboss.as.clustering.controller.OperationExecutor;
import org.jboss.as.clustering.msc.ServiceContainerHelper;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.dmr.ModelNode;
import org.wildfly.clustering.infinispan.spi.service.CacheServiceName;

/**
 * Executor for partition handling operations.
 * @author Paul Ferraro
 */
public class PartitionHandlingOperationExecutor implements OperationExecutor<AdvancedCache<?, ?>> {

    @Override
    public ModelNode execute(OperationContext context, Operation<AdvancedCache<?, ?>> operation) throws OperationFailedException {
        PathAddress address = context.getCurrentAddress();
        PathAddress cacheAddress = address.getParent();

        String cacheName = cacheAddress.getLastElement().getValue();
        String containerName = cacheAddress.getParent().getLastElement().getValue();

        Cache<?, ?> cache = ServiceContainerHelper.findValue(context.getServiceRegistry(true), CacheServiceName.CACHE.getServiceName(containerName, cacheName));

        return (cache != null) ? operation.execute(cache.getAdvancedCache()) : null;
    }
}
