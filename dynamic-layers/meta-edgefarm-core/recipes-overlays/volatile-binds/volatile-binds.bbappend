#
# /opt/cni/run shall be writeable. Required by flannel
# 

VOLATILE_BINDS += " \
    /data/opt_cni_run  /opt/cni/run\n\
"