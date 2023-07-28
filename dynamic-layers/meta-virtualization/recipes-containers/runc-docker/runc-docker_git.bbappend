#
# This is needed to allow docker to be configured to use the systemd cgroup driver (--exec-opt native.cgroupdriver=systemd)
#
# If runc is build for static mode, the following error is seen when running docker:
#   'docker: Error response from daemon: OCI runtime create failed: systemd cgroup flag passed, 
#    but systemd support for managing cgroups is not available: unknown.'
#
# So, disable static build. By default, PACKAGECONFIG is set to "static". 
# In that case, libcontainer/cgroups/systemd/apply_nosystemd.go is included in the runc build, which 
# simply returns an error when runc is called with the systemd cgroup driver.
#
# Building runc in dynamic mode, the apply_systemd.go file is included in the build, and runc works as expected.
#
PACKAGECONFIG=""