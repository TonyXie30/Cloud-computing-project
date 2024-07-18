# First setup a strogae class with provisioner as EBS.
kubectl apply -f ./Storage/ebs-sc.yaml

# Then we can now setup prometheus monitoring services for this cluster using helm.